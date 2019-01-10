package com.coin.exchange.database;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.anthonycr.bonsai.Completable;
import com.anthonycr.bonsai.Single;

import java.util.List;

public interface CollectionModel {

    @NonNull
    Single<CollectionItem> findCollectionForUrl(@NonNull String url);

    @NonNull
    Single<Boolean> isCollection(@NonNull String url);

    @NonNull
    Single<Boolean> addCollectionIfNotExists(@NonNull CollectionItem item);

    @NonNull
    Completable addCollectionList(@NonNull List<CollectionItem> bookmarkItems);

    @NonNull
    Single<Boolean> deleteCollection(@NonNull CollectionItem bookmark);

    @NonNull
    Completable deleteAllCollection();

    @NonNull
    Single<List<CollectionItem>> getAllCollection();

    @WorkerThread
    long count();
}
