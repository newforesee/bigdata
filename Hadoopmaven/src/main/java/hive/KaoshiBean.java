package hive;

import java.util.Arrays;

/**
 * Created by newforesee on 2018/9/8.
 */
public class KaoshiBean {
    private int t1;
    private char[] t2;
    private int t3;
    private char[] t4;
    private String t5;
    private int t6;
    private int t7;
    private int[] t8;
    private int t9;
    private int t10;

    public int getT1() {
        return t1;
    }

    public void setT1(int t1) {
        this.t1 = t1;
    }

    public char[] getT2() {
        return t2;
    }

    public void setT2(char[] t2) {
        this.t2 = t2;
    }

    public int getT3() {
        return t3;
    }

    public void setT3(int t3) {
        this.t3 = t3;
    }

    public char[] getT4() {
        return t4;
    }

    public void setT4(char[] t4) {
        this.t4 = t4;
    }

    public String getT5() {
        return t5;
    }

    public void setT5(String t5) {
        this.t5 = t5;
    }

    public int getT6() {
        return t6;
    }

    public void setT6(int t6) {
        this.t6 = t6;
    }

    public int getT7() {
        return t7;
    }

    public void setT7(int t7) {
        this.t7 = t7;
    }

    public int[] getT8() {
        return t8;
    }

    public void setT8(int[] t8) {
        this.t8 = t8;
    }

    public int getT9() {
        return t9;
    }

    public void setT9(int t9) {
        this.t9 = t9;
    }

    public int getT10() {
        return t10;
    }

    public void setT10(int t10) {
        this.t10 = t10;
    }

    @Override
    public String toString() {
        return "KaoshiBean{" +
                "t1=" + t1 +
                ", t2=" + Arrays.toString(t2) +
                ", t3=" + t3 +
                ", t4=" + Arrays.toString(t4) +
                ", t5='" + t5 + '\'' +
                ", t6=" + t6 +
                ", t7=" + t7 +
                ", t8=" + Arrays.toString(t8) +
                ", t9=" + t9 +
                ", t10=" + t10 +
                '}';
    }
}
