package com.coin.exchange.database;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.anthonycr.bonsai.Completable;
import com.anthonycr.bonsai.CompletableAction;
import com.anthonycr.bonsai.CompletableSubscriber;
import com.anthonycr.bonsai.Single;
import com.anthonycr.bonsai.SingleAction;
import com.anthonycr.bonsai.SingleSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;



public class CollectionDatabase extends SQLiteOpenHelper implements CollectionModel {

    private static final String TAG = "CollectionDatabase";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "CollectionManager";

    // HomeLabelItems table name
    private static final String TABLE_HOMELABEL = "collection";

    // HomeLabelItems Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_URL = "url";
    private static final String KEY_TITLE = "title";
    private static final String KEY_POSITION = "position";

    @Nullable
    private SQLiteDatabase mDatabase;

    @Inject
    public CollectionDatabase(@NonNull Application application) {
        super(application, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 初始化数据库
     *
     * @return
     */
    @WorkerThread
    @NonNull
    private synchronized SQLiteDatabase lazyDatabase() {
        if (mDatabase == null || !mDatabase.isOpen()) {
            mDatabase = getWritableDatabase();
        }
        return mDatabase;
    }

    // Creating Tables
    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String CREATE_BOOKMARK_TABLE = "CREATE TABLE " +
                DatabaseUtils.sqlEscapeString(TABLE_HOMELABEL) + '(' +
                DatabaseUtils.sqlEscapeString(KEY_ID) + " INTEGER PRIMARY KEY," +
                DatabaseUtils.sqlEscapeString(KEY_URL) + " TEXT," +
                DatabaseUtils.sqlEscapeString(KEY_TITLE) + " TEXT," +
                DatabaseUtils.sqlEscapeString(KEY_POSITION) + " INTEGER" + ')';
        db.execSQL(CREATE_BOOKMARK_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseUtils.sqlEscapeString(TABLE_HOMELABEL));
        // Create tables again
        onCreate(db);
    }

    @NonNull
    private static ContentValues bindHomeLabelToContentValues(@NonNull CollectionItem bookmarkItem) {
        ContentValues contentValues = new ContentValues(4);
        contentValues.put(KEY_URL, bookmarkItem.getUrl());
        contentValues.put(KEY_TITLE, bookmarkItem.getTitle());
        contentValues.put(KEY_POSITION, bookmarkItem.getPosition());
        return contentValues;
    }


    @NonNull
    private static CollectionItem bindCursorToHomeLabelItem(@NonNull Cursor cursor) {
        CollectionItem bookmark = new CollectionItem();
        bookmark.setUrl(cursor.getString(cursor.getColumnIndex(KEY_URL)));
        bookmark.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
        bookmark.setPosition(cursor.getInt(cursor.getColumnIndex(KEY_POSITION)));

        return bookmark;
    }

    @NonNull
    private static List<CollectionItem> bindCursorToHomeLabelItemList(@NonNull Cursor cursor) {
        List<CollectionItem> bookmarks = new ArrayList<>();

        while (cursor.moveToNext()) {
            bookmarks.add(bindCursorToHomeLabelItem(cursor));
        }

        cursor.close();

        return bookmarks;
    }


    @NonNull
    private static String alternateSlashUrl(@NonNull String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        } else {
            return url + '/';
        }
    }

    @NonNull
    private Cursor queryWithOptionalEndSlash(@NonNull String url) {
        Cursor cursor = lazyDatabase().query(TABLE_HOMELABEL, null, KEY_URL + "=?", new String[]{url}, null, null, null, "1");

        if (cursor.getCount() == 0) {
            cursor.close();
            String alternateUrl = alternateSlashUrl(url);
            cursor = lazyDatabase().query(TABLE_HOMELABEL, null, KEY_URL + "=?", new String[]{alternateUrl}, null, null, null, "1");
        }

        return cursor;
    }


    private int deleteWithOptionalEndSlash(@NonNull String url) {
        int deletedRows = lazyDatabase().delete(TABLE_HOMELABEL, KEY_URL + "=?", new String[]{url});

        if (deletedRows == 0) {
            String alternateUrl = alternateSlashUrl(url);
            deletedRows = lazyDatabase().delete(TABLE_HOMELABEL, KEY_URL + "=?", new String[]{alternateUrl});
        }

        return deletedRows;
    }

    @NonNull
    @Override
    public Single<CollectionItem> findCollectionForUrl(@NonNull final String url) {
        return Single.create(new SingleAction<CollectionItem>() {
            @Override
            public void onSubscribe(@NonNull SingleSubscriber<CollectionItem> subscriber) {
                Cursor cursor = queryWithOptionalEndSlash(url);

                if (cursor.moveToFirst()) {
                    subscriber.onItem(bindCursorToHomeLabelItem(cursor));
                } else {
                    subscriber.onItem(null);
                }

                cursor.close();
                subscriber.onComplete();
            }
        });
    }

    @NonNull
    @Override
    public Single<Boolean> isCollection(@NonNull final String url) {
        return Single.create(new SingleAction<Boolean>() {
            @Override
            public void onSubscribe(@NonNull SingleSubscriber<Boolean> subscriber) {
                Cursor cursor = queryWithOptionalEndSlash(url);

                subscriber.onItem(cursor.moveToFirst());

                cursor.close();
                subscriber.onComplete();
            }
        });
    }

    @NonNull
    @Override
    public Single<Boolean> addCollectionIfNotExists(@NonNull final CollectionItem item) {
        return Single.create(new SingleAction<Boolean>() {
            @Override
            public void onSubscribe(@NonNull SingleSubscriber<Boolean> subscriber) {
                Cursor cursor = queryWithOptionalEndSlash(item.getUrl());

                if (cursor.moveToFirst()) {
                    cursor.close();
                    subscriber.onItem(false);
                    subscriber.onComplete();
                    return;
                }

                cursor.close();

                long id = lazyDatabase().insert(TABLE_HOMELABEL, null, bindHomeLabelToContentValues(item));

                subscriber.onItem(id != -1);
                subscriber.onComplete();
            }
        });
    }

    @NonNull
    @Override
    public Completable addCollectionList(@NonNull final List<CollectionItem> bookmarkItems) {
        return Completable.create(new CompletableAction() {
            @Override
            public void onSubscribe(@NonNull CompletableSubscriber subscriber) {
                lazyDatabase().beginTransaction();

                for (CollectionItem item : bookmarkItems) {
                    addCollectionIfNotExists(item).subscribe();
                }

                lazyDatabase().setTransactionSuccessful();
                lazyDatabase().endTransaction();

                subscriber.onComplete();
            }
        });
    }

    @NonNull
    @Override
    public Single<Boolean> deleteCollection(@NonNull final CollectionItem bookmark) {
        return Single.create(new SingleAction<Boolean>() {
            @Override
            public void onSubscribe(@NonNull SingleSubscriber<Boolean> subscriber) {
                int rows = deleteWithOptionalEndSlash(bookmark.getUrl());

                subscriber.onItem(rows > 0);
                subscriber.onComplete();
            }
        });
    }

    @NonNull
    @Override
    public Completable deleteAllCollection() {
        return Completable.create(new CompletableAction() {
            @Override
            public void onSubscribe(@NonNull CompletableSubscriber subscriber) {
                lazyDatabase().delete(TABLE_HOMELABEL, null, null);

                subscriber.onComplete();
            }
        });
    }


    @NonNull
    @Override
    public Single<List<CollectionItem>> getAllCollection() {
        return Single.create(new SingleAction<List<CollectionItem>>() {
            @Override
            public void onSubscribe(@NonNull SingleSubscriber<List<CollectionItem>> subscriber) {
                Cursor cursor = lazyDatabase().query(TABLE_HOMELABEL, null, null, null, null, null, null);

                subscriber.onItem(bindCursorToHomeLabelItemList(cursor));
                subscriber.onComplete();
            }
        });
    }


    @Override
    public long count() {
        try {
            return DatabaseUtils.queryNumEntries(lazyDatabase(), TABLE_HOMELABEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
