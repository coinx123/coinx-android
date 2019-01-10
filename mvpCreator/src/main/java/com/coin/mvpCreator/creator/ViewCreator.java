package com.coin.mvpCreator.creator;

import com.squareup.javapoet.TypeSpec;
import com.coin.mvpCreator.utils.ClzNameHelper;
import com.coin.mvpCreator.utils.JavaPencil;

import java.io.IOException;

import javax.lang.model.element.Modifier;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/12/11
 * @description mvp中view
 */
public class ViewCreator {

    // 包名
    private final String pkgName;
    // 类名
    private final String clzName;
    // 存放路径
    private final String path;

    public ViewCreator(String path,
                       String pkgName) {
        this.path = path;
        this.clzName = ClzNameHelper.getInstance().getName(ClzNameHelper.VIEW);
        this.pkgName = pkgName;
    }

    public void create() throws IOException {

        // 生成接口类
        TypeSpec typeSpec = TypeSpec
                .interfaceBuilder(clzName)      // 类名
                .addModifiers(Modifier.PUBLIC)  // 类型限定付
                .addSuperinterface(ClzNameHelper.getInstance().getClass(ClzNameHelper.VIEW)) // 继承IView类
                .build();

        JavaPencil.write(path, pkgName, typeSpec);
    }


}
