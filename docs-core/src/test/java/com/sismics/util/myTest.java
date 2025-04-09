package com.sismics.util;

import org.junit.Assert;
import org.junit.Test;
import com.sismics.util.css.Rule;
import com.sismics.util.EnvironmentUtil;

public class myTest {
    @Test
    public void testCSS() {
        Rule rule = new Rule("color", "red");
        Assert.assertEquals("color: red", rule.toString());
    }

    @Test
    public void testOS() {
        boolean isMac = EnvironmentUtil.isMacOs();
        boolean isWin = EnvironmentUtil.isWindows();
        boolean isUnix = EnvironmentUtil.isUnix();
    }

}