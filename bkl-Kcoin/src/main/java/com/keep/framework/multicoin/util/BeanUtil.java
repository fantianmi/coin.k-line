package com.keep.framework.multicoin.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class BeanUtil {

	@SuppressWarnings("rawtypes")
	public static void toBean(Object bean, Map prop) {
		try {
			BeanUtils.populate(bean, prop);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
