package com.moocsk.lowcode.connector;

import java.util.ArrayList;
import java.util.List;

import com.moocsk.lowcode.api.TransApi;
import com.moocsk.lowcode.model.TranslateResult;
import com.moocsk.lowcode.model.TranslateResultSingle;
import com.moocsk.lowcode.util.ModelUtil;
import com.netease.lowcode.core.annotation.NaslConnector;

/**
 * 百度翻译连接器
 */
@NaslConnector(connectorKind = "BaiduTranslate")
public class BaiduTranslate {

    /** 应用ID */
    private String appid;

    /** 密钥 */
    private String secretKey;

    /**
     * 初始化
     * @param appid 应用ID
     * @param secretKey 密钥
     * @return 百度翻译连接器
     */
    @NaslConnector.Creator
    public BaiduTranslate init(String appid, String secretKey) {
        BaiduTranslate baiduTranslate = new BaiduTranslate();
        baiduTranslate.appid = appid;
        baiduTranslate.secretKey = secretKey;
        return baiduTranslate;
    }

    /**
     * 连通性测试
     * @param appid 应用ID
     * @param secretKey 密钥
     * @return 连通结果
     */
    @NaslConnector.Tester
    public Boolean testConnection(String appid, String secretKey) {
        TransApi transApi = new TransApi(appid, secretKey);
        TranslateResult translateResult = transApi.generalTextTranslation("测试", "zh", "en");
        if (translateResult.errorCode == null) {
            return true;
        }
        return false;
    }

    /**
     * 单条文本翻译
     * @param q 需要翻译的内容
     * @param from 源语言
     * @param to 目标语言
     * @return 单条翻译结果
     */
    @NaslConnector.Logic
    public TranslateResultSingle translation(String q, String from, String to) {
        TransApi transApi = new TransApi(this.appid, this.secretKey);
        TranslateResult translateResult = transApi.generalTextTranslation(q, from, to);
        TranslateResultSingle translateResultSingle = ModelUtil.getSingleTranslate(translateResult);
        return translateResultSingle;
    }

    /**
     * 批量文本翻译
     * @param q 需要翻译的内容
     * @param from 源语言
     * @param to 目标语言
     * @return 批量翻译结果
     */
    @NaslConnector.Logic
    public TranslateResult translationBatch(List<String> q, String from, String to) {
        String qStr = String.join("\n", q);
        TransApi transApi = new TransApi(this.appid, this.secretKey);
        return transApi.generalTextTranslation(qStr, from, to);
    }

    public static void main(String[] args) {
        String appid = "";
        String secretKey = "";
        String from = "zh";
        String to = "en";

        BaiduTranslate baiduTranslate = new BaiduTranslate().init(appid, secretKey);

        // 注意：以下测试片段不能同时调用

        // 测试连通性
        Boolean connBoolean = baiduTranslate.testConnection(appid, secretKey);
        if (connBoolean) {
            System.out.println("连接成功");
        } else {
            System.out.println("连接失败");
        }

        // 测试批量文本翻译
        // List<String> list = new ArrayList<String>();
        // list.add("水果");
        // list.add("香蕉");
        // TranslateResult translateResult = baiduTranslate.translationBatch(list, from, to);
        // System.out.println("批量文本翻译结果");
        // System.out.println(translateResult);

        // 测试单条文本翻译
        // TranslateResultSingle translateResultSingle = baiduTranslate.translation("水果", from, to);
        // System.out.println("单条文本翻译结果");
        // System.out.println(translateResultSingle);
     }
    
}
