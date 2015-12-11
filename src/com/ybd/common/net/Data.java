/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.ybd.common.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ybd.common.tools.StringUtil;

/**
 * 手机向服务端提交的数据<br>
 * 需要上传文件时，加path
 * 
 * @author cyf
 * @version $Id: Data.java, v 0.1 Apr 22, 2013 8:44:29 AM cyf Exp $
 */
public class Data {

    private String                    url;

    private Map<String, String>       datas;

    private Map<String, List<String>> datasArray;

    private List<String>              path;

    /**
     * 增加一个提交服务器的参数,数组类型的
     * 
     * @param key 参数的key
     * @param data 参数值
     */
    public void addDataArray(String key, List<String> data) {
        if (null == datasArray) {
            datasArray = new HashMap<String, List<String>>();
        }
        List<String> d = datasArray.get(key);
        if (null == d) {
            datasArray.put(key, data);
        } else {
            d.addAll(data);
            datasArray.put(key, d);
        }
    }

    /**
     * 增加一个提交服务器的参数,数组类型的
     * 
     * @param key 参数的key
     * @param data 参数值
     */
    public void addDataArray(String key, Object data) {
        if (null == datasArray) {
            datasArray = new HashMap<String, List<String>>();
        }
        List<String> d = datasArray.get(key);
        if (null == d) {
            d = new ArrayList<String>();
        }
        d.add(String.valueOf(data));
        datasArray.put(key, d);
    }

    /**
     * 增加一个提交服务器的参数
     * 
     * @param key 参数的key
     * @param data 参数值
     */
    public void addData(String key, Object data) {
        if (null == datas) {
            datas = new HashMap<String, String>();
        }
        datas.put(key, String.valueOf(data));
    }

    /**
     * 增加一个提交服务器的参数,参数用“,”隔开
     * 
     * @param key 参数的key
     * @param data 参数值
     */
    public void addDataSeparator(String key, Object data) {
        if (null == datas) {
            datas = new HashMap<String, String>();
        }
        String value = datas.get(key);
        if (StringUtil.isNotBlank(value)) {
            value = value.concat(",").concat(String.valueOf(data));
        } else {
            value = String.valueOf(data);
        }
        datas.put(key, value);
    }

    /**
     * 增加一个上传文件的路径
     * 
     * @param fpath 要上传文件的路径
     */
    public void addPath(Object fpath) {
        if (null == path) {
            path = new ArrayList<String>();
        }
        path.add(String.valueOf(fpath));
    }

    public Data(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, String> datas) {
        this.datas = datas;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public Map<String, List<String>> getDatasArray() {
        return datasArray;
    }

    public void setDatasArray(Map<String, List<String>> datasArray) {
        this.datasArray = datasArray;
    }

}
