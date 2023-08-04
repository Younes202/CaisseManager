package framework.model.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import framework.controller.ContextGloabalAppli;

public class BigDecimalUtil {
	private final static int ROUND_MODE = BigDecimal.ROUND_HALF_EVEN;///RoundingMode.FLOOR;
	public static BigDecimal ZERO = new BigDecimal("0");

	public static boolean isZero(BigDecimal val) {
		if(val == null || val.compareTo(ZERO) == 0){
			return true;
		}
		return false;
	}
	public static String getStyle(BigDecimal val) {
		if(val == null || val.compareTo(ZERO) == 0){
			return "";
		}
		return val.compareTo(BigDecimalUtil.ZERO) < 0 ? "red":"green";
	}
	
	public static BigDecimal get(int val) {
		return new BigDecimal(val);
	}
	
	public static BigDecimal get(String val) {
		if(StringUtil.isEmpty(val)){
			return ZERO;
		}
		
		val = val.replace('�', 'x');
		val = val.replaceAll("x", "");
		val = val.replaceAll("\\s+", "");
		val = val.replaceAll(",", ".");
		
		val = NumericUtil.replaceBlank(val);
		
		try{
			return new BigDecimal(val);
		} catch (Exception e){
			return BigDecimalUtil.get(0);
		}
	}
	
	public static BigDecimal round(BigDecimal value) {
		if(value == null){
			value = get("0.00");
		}
		return value.setScale(ContextGloabalAppli.getNbrDecimal(), ROUND_MODE);
	}
	
	public static BigDecimal divide(BigDecimal value1, BigDecimal value2){
		if(value1 == null){
			value1 = ZERO;
		}
		if(value2 == null){
			value2 = get(1);
		}
		// Dévision avec précision 4 puis arroundi à 2
		return value1.divide(value2, 6, ROUND_MODE);//, MathContext.DECIMAL128
	}
	
	public static BigDecimal add(BigDecimal ... values){
		BigDecimal total = ZERO;
		for (BigDecimal val : values) {
			if(val == null){
				val = ZERO;
			}
			
			total = total.add(val);	
		}
		
		return total;
	}
	
	public static BigDecimal substract(BigDecimal ... values){
		BigDecimal total = values[0]!=null?values[0]:ZERO;
		int idx = 0;
		for (BigDecimal val : values) {
			if(idx != 0){
				if(val == null){
					val = ZERO;
				}
				
				total = total.subtract(val);
			}
			idx++;
		}
		
		return total; 
	}
	
	public static BigDecimal multiply(BigDecimal value1, BigDecimal value2){
		if(value1 == null){
			value1 = ZERO;
		}
		if(value2 == null){
			value2 = ZERO;
		}
		return value1.multiply(value2);
	}

	public static String formatNumberZeroBd(BigDecimal bd){
		String mtt = formatNumber(bd);
		
		return StringUtil.isEmpty(mtt) ? formatNumber(get("0.00")) : mtt;
	}
	public static String formatNumberZeroBlank(BigDecimal bd){
		String mtt = formatNumber(bd);
		
		return (StringUtil.isEmpty(mtt) || mtt.equals("0.00")|| mtt.equals("0,00"))? "" : mtt;
	}
	
	public static String formatNumberZero(BigDecimal bd){
		String mtt = formatNumber(bd);
		
		return StringUtil.isEmpty(mtt) ? formatNumber(get("0.00")) : mtt;
	}
	
	public static String formatNumber(BigDecimal bd){
        if(bd != null){
        	int nbrDec = ContextGloabalAppli.getNbrDecimal();
            bd = bd.setScale(nbrDec, BigDecimal.ROUND_DOWN);
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(nbrDec);
            df.setMinimumFractionDigits(nbrDec);
            df.setGroupingUsed(true);
            
            return df.format(bd);
        }
        return "";
    }
	
	public static String formatNumberZero(BigDecimal bd, Integer nbrDec){
		if(nbrDec == null || nbrDec == 0) {
			nbrDec = ContextGloabalAppli.getNbrDecimalSaisie();
		}
		String mtt = formatNumber(bd, nbrDec);
		
		return StringUtil.isEmpty(mtt) ? formatNumber(get("0.00"), nbrDec) : mtt;
	}
	
	public static String formatNumber(BigDecimal bd, int nbrDec){
        if(bd != null){
            bd = bd.setScale(nbrDec, BigDecimal.ROUND_DOWN);
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(nbrDec);
            df.setMinimumFractionDigits(nbrDec);
            df.setGroupingUsed(true);
            
            String val = df.format(bd);
			return val.replaceAll(",", ".");
        }
        return "";
    }
	
	public static BigDecimal roundCustom(BigDecimal mttNet) {
		if(mttNet == null || isZero(mttNet)) {
			return mttNet;
		}
		
		BigDecimal lastInt = BigDecimalUtil.get(mttNet.toString().substring(mttNet.toString().indexOf(".")-1));
		boolean isToAdjust = false;
		double valToAddSubstract = 0;
		if((lastInt.compareTo(ZERO) > 0 && lastInt.compareTo(BigDecimalUtil.get("2.5"))<0) || 
						(lastInt.compareTo(get("5"))>0 && lastInt.compareTo(get("7.5"))<0)){
			valToAddSubstract = Math.floor(divide(lastInt, get("5")).doubleValue()) * 5;
			isToAdjust = true;
		} else if((lastInt.compareTo(get("2.5"))>=0 && lastInt.compareTo(get("5"))<0) || lastInt.compareTo(get("7.5"))>=0){
			valToAddSubstract = Math.ceil(divide(lastInt, get("5")).doubleValue()) * 5;
			isToAdjust = true;
		}
		//
		if(isToAdjust){
			if(mttNet.compareTo(ZERO) < 0){
				valToAddSubstract = valToAddSubstract*-1;
			}
			
			String realPart = mttNet.toString().substring(0, mttNet.toString().indexOf(".")-1)+"0";
			mttNet = BigDecimalUtil.add(get(realPart), get(""+valToAddSubstract));
		}
		
		return mttNet; 
	}
	
	public static String formatNumber(Integer in){
		if(in == null){
			return "";
		}
         DecimalFormat df = new DecimalFormat();
         df.setMaximumFractionDigits(0);
         df.setMinimumFractionDigits(0);
         df.setGroupingUsed(true);
         
        return df.format(in);
	}
	
	public static String formatNumberZero(Integer in){
		String mtt = formatNumber(in);
		return StringUtil.isEmpty(mtt) ? "0" : mtt;
	}

	public static BigDecimal negate(BigDecimal mtt_donne) {
		return (mtt_donne != null ? mtt_donne.negate() : null);
	}
	
	/**
	 * Calcul hors taxe
	 * @param mtt_donne
	 * @return
	 */
	public static BigDecimal getMttHt(BigDecimal mtt_ttc, BigDecimal taux) {
		if(taux == null){
			taux = BigDecimalUtil.ZERO;
		}
		if(mtt_ttc == null){
			mtt_ttc = BigDecimalUtil.ZERO;
		}
		BigDecimal mttHt = BigDecimalUtil.divide(
			BigDecimalUtil.multiply(mtt_ttc, BigDecimalUtil.get(100)),
			BigDecimalUtil.add(BigDecimalUtil.get("100"), taux));
		
		return mttHt;
	}
}
