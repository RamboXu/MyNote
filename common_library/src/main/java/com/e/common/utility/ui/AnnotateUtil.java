package com.e.common.utility.ui;

import java.lang.reflect.Field;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AnnotateUtil {
	/**
	 * @param currentClass
	 *            当前类，一般为Activity或Fragment
	 * @param sourceView
	 *            待绑定控件的直接或间接父控件
	 */
	public static void initBindView(Object currentClass, View sourceView) {
		// 通过反射获取到全部属性，反射的字段可能是一个类（静态）字段或实例字段
		Field[] fields = currentClass.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				// 返回BindView类型的注解内容
				BindView bindView = field.getAnnotation(BindView.class);
				if (bindView != null) {
					int viewId = bindView.id();
					boolean clickLis = bindView.click();
					try {
						field.setAccessible(true);
						View currentView = sourceView.findViewById(viewId);
						if (clickLis) {
							currentView
									.setOnClickListener((OnClickListener) currentClass);
						}
						// 将currentClass的field赋值为sourceView.findViewById(viewId)
						field.set(currentClass, currentView);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 必须在setContentView之后调用
	 * 
	 * @param aty
	 */
	public static void initBindView(Activity aty) {
		initBindView(aty, aty.getWindow().getDecorView());
	}

	/**
	 * 必须在setContentView之后调用
	 * 
	 * @param frag
	 */
	public static void initBindView(Fragment frag) {
		initBindView(frag, frag.getActivity().getWindow().getDecorView());
	}
}
