/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.ybd.common.net;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import com.ybd.common.C;

/**
 * 上传文件进度条相关的类。
 * 可以通过回调清楚的知道文件目前上传了多少，而不用像服务端做进度条那样去后台请求当前文件上传了
 * 
 * @author cyf
 * @version $Id: CustomMultipartEntity.java, v 0.1 2013-7-3 下午4:56:17 cyf Exp $
 */
public class CustomMultiPartEntity extends MultipartEntity {

    private final ProgressListener listener;

    public CustomMultiPartEntity(final ProgressListener listener) {
        super(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName(C.CHARSET));
        this.listener = listener;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.listener));
    }

    @Override
    protected String generateContentType(String boundary, Charset charset) {
        return super.generateContentType(boundary, charset);
    }

    public static interface ProgressListener {
        void transferred(long num);
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private final ProgressListener listener;
        private long                   transferred;

        public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
            super(out);
            this.listener = listener;
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            this.transferred += len;
            this.listener.transferred(this.transferred);
        }

        public void write(int b) throws IOException {
            out.write(b);
            this.transferred++;
            this.listener.transferred(this.transferred);
        }
    }
}