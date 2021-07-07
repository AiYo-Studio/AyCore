package com.mc9y.blank038api.util.custom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 广告客户端
 *
 * @author Blank038
 * @since 2021-05-13
 */
public class ADClient {
    private final List<String> AD_LIST = new ArrayList<>();
    private boolean error;

    public ADClient(String targetUrl) {
        try {
            URL url = new URL(targetUrl);
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String gg;
            while ((gg = br.readLine()) != null) {
                this.AD_LIST.add(gg);
            }
            br.close();
            isr.close();
            is.close();
        } catch (IOException ignored) {
            error = true;
        }
    }

    /**
     * 获取过程是否异常
     *
     * @return 是否异常
     */
    public boolean isError() {
        return this.error;
    }

    /**
     * 获取广告列表
     *
     * @return 广告列表
     */
    public List<String> getADList() {
        return this.AD_LIST;
    }
}
