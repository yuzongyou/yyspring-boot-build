package com.duowan.common.web.view;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Arvin
 */
public class SecurityAbstractUrlBasedView extends AbstractUrlBasedView {

    /**
     * 所属视图
     */
    private AbstractTextView ownerView;

    public SecurityAbstractUrlBasedView(AbstractTextView ownerView) {
        this.ownerView = ownerView;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String body;
        body = this.ownerView.getBody(model, request, response);

        if (!this.ownerView.isXssChecked()) {
            // 没有进行 XSS 检查，那么就进行检查并且替换掉 XSS 内容
            body = filterXssThenReplace(logger, body);
        }

        // 输出内容
        writeBody(body, response);

    }

    /**
     * 输出内容
     *
     * @param body     要输出的内容
     * @param response 响应
     */
    private void writeBody(String body, HttpServletResponse response) throws IOException {
        response.setContentType(this.ownerView.getContentType());

        String encoding = this.ownerView.getContentEncoding();

        if (StringUtils.isBlank(encoding)) {
            encoding = Charset.defaultCharset().name();
        }

        response.setContentLength(body.getBytes(encoding).length);

        Writer out = response.getWriter();
        out.write(body);
        out.flush();
    }

    /**
     * 进行检查并且替换掉 XSS 内容
     *
     * @param logger 日志
     * @param body   要检测的字符串内容
     * @return
     */
    private String filterXssThenReplace(Log logger, String body) {
        // XSS filter 过滤
        return body;
    }
}
