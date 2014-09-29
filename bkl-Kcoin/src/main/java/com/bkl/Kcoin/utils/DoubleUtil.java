package com.bkl.Kcoin.utils;

import java.math.BigDecimal;

import com.bkl.Kcoin.CoinConfig;
import com.km.common.utils.DecimalUtil;

public class DoubleUtil {
	
	public static double formatDouble(double value) {
		return DecimalUtil.formatDouble(value);
	}
	
	public static void print(double value) {
		BigDecimal ba = new BigDecimal(Double.toString(value));
		System.out.println(ba);
	}
	
	public static double add(double a,double b) {
		return DecimalUtil.add(a, b);
	}
	public static double subtract(double a,double b) {
		return DecimalUtil.subtract(a, b);
	}
	
	public static double multiply(double a,double b) {
		return DecimalUtil.multiply(a, b);
	}
	
	public static double divide(double a,double b) {
		return DecimalUtil.divide(a, b);
	}
	
	public static boolean larger(double a, double b) {
		return DecimalUtil.larger(a, b);
	}
	public static boolean equal(double a, double b) {
		return DecimalUtil.equal(a, b);
	}
	public static boolean lesser(double a, double b) {
		return DecimalUtil.lesser(a, b);
	}
/*	public static double divide(double a,double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		//ba = ba.setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP);
		BigDecimal bb = new BigDecimal(Double.toString(b));
		//bb = bb.setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP);
		BigDecimal bc = ba.divide(bb);
		bc = bc.setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP);
		return bc.doubleValue();
	}*/
	
	public static double getNumberByDecimalPrecision(int precision) {
		return DecimalUtil.getNumberByDecimalPrecision(precision);
	}
	
	public static boolean isMultiple(double number, double unit) {
		return DecimalUtil.isMultiple(number, unit);
	}
	
	public static boolean exceedPrecision(double number, int precision) {
		return DecimalUtil.exceedPrecision(number, precision);
	}
	
	public static void main(String[] args) {
		double a = 0.12349;
		
		double b = 0.0000000000000000000000000000000000000000000000000000000051;
		//System.out.println((a -b) == 0);
		//print(DoubleUtil.formatDouble(a));
		/*
		print(BigDecimalUtil.add(a, b));
		print(BigDecimalUtil.subtract(a, b));
		print(BigDecimalUtil.multiply(a, b));*/
/*		System.out.println(DoubleUtil.larger(a, b));
		System.out.println(DoubleUtil.equal(a, b));
		System.out.println(DoubleUtil.lesser(a, b));
		
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(3));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(2));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(1));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(0));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(-1));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(-2));
		System.out.println(DoubleUtil.getNumberByDecimalPrecision(-3));*/
/*		System.out.println(DoubleUtil.getNumberByDecimalPrecision(3));
		System.out.println(DoubleUtil.isMultiple(0.02, 0.01));
		System.out.println(DoubleUtil.isMultiple(0.1, 0.01));
		System.out.println(DoubleUtil.isMultiple(0.12, 0.01));
		System.out.println(DoubleUtil.isMultiple(100, 0.01));
		System.out.println(DoubleUtil.isMultiple(0.0132, 0.01));*/
		
		System.out.println(exceedPrecision(1.1, 2));
		System.out.println(exceedPrecision(1.10, 2));
		System.out.println(exceedPrecision(1, 2));
		System.out.println(exceedPrecision(11, 2));
		System.out.println(exceedPrecision(11, 2));
		System.out.println(exceedPrecision(1.11, 2));
		System.out.println(exceedPrecision(1.1100, 2));
		System.out.println(exceedPrecision(1.1110, 2));
		System.out.println(exceedPrecision(1.11111111111111111111111111111111, 2));
	}
}
