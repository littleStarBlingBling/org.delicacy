package com.delicacy.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.thymeleaf.util.StringUtils;

/**
 * @Author: MyDear
 */
public class UsernameTransferUtil {

    private static String PINYIN_REGEX = "[\\u4e00-\\u9fa5]+";

    public static String transferToPinyin(String username) {
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        char[] array = username.trim().toCharArray();

        StringBuilder stringBuilder = new StringBuilder();

        try {
            for (int i = 0; i < username.length(); i++) {
                if (String.valueOf(array[i]).matches(PINYIN_REGEX)) {
                    stringBuilder.append(PinyinHelper.toHanyuPinyinStringArray(array[i], defaultFormat)[0]);
                } else {
                    stringBuilder.append(array[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        if (StringUtils.isEmpty(stringBuilder.toString())) {
            return username;
        }

        return stringBuilder.toString();
    }

}
