package cubesystem.kurimabank.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kurimabank-kurima on 2016/03/22.
 */
public class Info {
    public Action action;
    public String reason = "";
    public String detail = "";
    public MyContact myContact = new MyContact("default");
    public ArrayList<Contact> allContact = new ArrayList<>(
            Arrays.asList(new Contact[]{
                    new Contact("1", Contact.SendType.TO),
                    new Contact("2", Contact.SendType.CC),
                    new Contact("3", Contact.SendType.CC),
                    new Contact("4", Contact.SendType.CC),
                    new Contact("5", Contact.SendType.CC)
            }));

    private static Info _instance = new Info();
    public static Info getInstance(){
        return _instance;
    }
    /**
     * その他の場合、詳細情報が入力されているかどうかチェックする
     */
    //public static boolean isBlank(String detail) {
        //return (str == null || Info.trimSpace(str.trim()).length() == 0);
    //}

    /**
     * 有効な文字列が入力されているかどうかチェックする
     */
    public static boolean isBlank(String str) {
        return (str == null || Info.trimSpace(str.trim()).length() == 0);
    }

    // 全角スペース対応用
    private static String trimSpace(String orgStr) {
        char[] value = orgStr.toCharArray();
        int len = value.length;
        int st = 0;
        char[] val = value;

        while ((st < len) && (val[st] <= ' ' || val[st] == '　')) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= ' ' || val[len - 1] == '　')) {
            len--;
        }

        return ((st>0) || (len<value.length)) ? orgStr.substring(st,len):orgStr;
    }
}
