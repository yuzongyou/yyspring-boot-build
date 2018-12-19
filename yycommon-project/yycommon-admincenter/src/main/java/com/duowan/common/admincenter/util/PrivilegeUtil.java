package com.duowan.common.admincenter.util;

import com.duowan.common.admincenter.exception.PrivilegeParseException;
import com.duowan.common.admincenter.model.Privilege;
import com.duowan.common.admincenter.model.PrivilegeItem;
import com.duowan.common.exception.CodeException;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.Encodings;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 权限工具类
 *
 * @author Arvin
 * @since 2018/08/27 11:55
 */
public class PrivilegeUtil {

    private PrivilegeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DEFAULT_PRIVILEGE_XML_PATH = "/privilege.xml";

    /**
     * 读取为string
     *
     * @param privilegeXmlPath 权限xml文件路径， 默认使用classpath:privilege.xml
     * @return 返回权限文件内容
     */
    public static String loadPrivilegeXmlAsString(String privilegeXmlPath) {
        try {
            String path = StringUtils.isBlank(privilegeXmlPath) ? DEFAULT_PRIVILEGE_XML_PATH : privilegeXmlPath;
            File file = new File(path);
            InputStream inputStream = null;
            if (file.exists()) {
                inputStream = new FileInputStream(file);
            } else {
                ClassPathResource resource = new ClassPathResource(path);
                if (resource.exists()) {
                    inputStream = resource.getInputStream();
                } else {
                    throw new CodeException(500, "权限文件[" + resource.getPath() + "] 不存在！");
                }
            }

            return IOUtils.toString(new InputStreamReader(inputStream, Encodings.DEFAULT_ENCODING));
        } catch (Exception e) {
            throw new PrivilegeParseException(e);
        }
    }

    /**
     * 根据权限XML格式文本解析权限列表
     *
     * @param xmlText   XML 格式的权限列表，通常需要传入 classpath:privilege.xml
     * @param refParent 是否指向父级
     * @return 返回权限根对象列表
     */
    private static List<Privilege> parseByXml(String xmlText, boolean refParent) {

        AssertUtil.assertNotBlank(xmlText, "权限XML文本不能为空");

        String pureXmlText = removeXmlBOM(xmlText);
        List<Privilege> privilegeList = null;
        try {
            Document document = DocumentHelper.parseText(pureXmlText);

            privilegeList = new ArrayList<>();

            parsePrivilegeFromPrivilegeXml(document.getRootElement(), null, privilegeList, refParent);

        } catch (DocumentException e) {
            throw new PrivilegeParseException(e);
        }

        return privilegeList;
    }

    /**
     * 根据权限XML格式文本解析权限列表, 子权限将能访问父级
     *
     * @param xmlText XML 格式的权限列表，通常需要传入 classpath:privilege.xml
     * @return 返回权限根对象列表
     */
    public static List<Privilege> parseByXmlRefParent(String xmlText) {
        return parseByXml(xmlText, true);
    }

    /**
     * 根据权限XML格式文本解析权限列表, 自权限将 不能 访问父级
     *
     * @param xmlText XML 格式的权限列表，通常需要传入 classpath:privilege.xml
     * @return 返回权限根对象列表
     */
    public static List<Privilege> parseByXmlUnRefParent(String xmlText) {
        return parseByXml(xmlText, false);
    }

    /**
     * 根据权限XML格式文本解析权限列表并且转成 js 对象
     *
     * @param xmlText XML 格式的权限列表，通常需要传入 classpath:privilege.xml
     * @return 解析成map
     */
    public static Map<String, Object> parseAndToJSObject(String xmlText) {
        return toJavaScriptObject(parseByXml(xmlText, false));
    }

    /**
     * 转成 javascript 对象
     *
     * @param privilegeList 权限列表
     * @return 解析成map
     */
    public static Map<String, Object> toJavaScriptObject(List<Privilege> privilegeList) {

        AssertUtil.assertNotEmpty(privilegeList, "权限列表为空，无法转成 JS 对象");

        Map<String, Object> resultMap = new HashMap<>();

        for (Privilege privilege : privilegeList) {

            if (resultMap.containsKey(privilege.getPrivilegeId())) {
                throw new CodeException(500, "根权限集下面的权限标识ID[" + privilege.getPrivilegeId() + "]已存在，privilegeId=" + privilege.getPrivilegeId());
            }

            Map<String, Object> subMap = new HashMap<>();

            convertToJavaScriptReadablePrivileges(privilege, subMap);

            resultMap.put(privilege.getPrivilegeId(), subMap);
        }

        return resultMap;
    }

    public static List<Map<String, Object>> toAllMenus(List<Privilege> allTopPrivileges) {
        return toMenus(allTopPrivileges, null, true);
    }

    /**
     * 转换成菜单
     *
     * @param allTopPrivileges    所有权限
     * @param includePrivilegeIds 包含的权限ID列表
     * @return 解析成list map
     */
    public static List<Map<String, Object>> toMenus(List<Privilege> allTopPrivileges, Set<String> includePrivilegeIds) {
        return toMenus(allTopPrivileges, includePrivilegeIds, false);
    }

    private static List<Map<String, Object>> toMenus(List<Privilege> allTopPrivileges, Set<String> includePrivilegeIds, boolean ignoreInclude) {
        List<Map<String, Object>> menus = new ArrayList<>();

        if (null != allTopPrivileges && !allTopPrivileges.isEmpty()) {
            filterMenuPrivileges(includePrivilegeIds, ignoreInclude, allTopPrivileges, menus);
        }

        return menus;
    }

    private static boolean isIncludePrivilegeId(Set<String> includePrivilegeIds, String privilegeId) {

        return null != includePrivilegeIds && includePrivilegeIds.contains(privilegeId);

    }

    /**
     * 转换成 菜单
     *
     * @param privilege           权限对象
     * @param ignoreInclude       是否包含
     * @param includePrivilegeIds 包含的权限ID集合
     * @return 返回map
     */
    private static Map<String, Object> convertToMenu(Privilege privilege, Set<String> includePrivilegeIds, boolean ignoreInclude) {

        if (null == privilege || !privilege.isShowMenu()) {
            return null;
        }

        Map<String, Object> menuMap = new HashMap<>();

        menuMap.put("text", privilege.getName());
        if (StringUtils.isNotBlank(privilege.getCssClass())) {
            menuMap.put("cssClass", privilege.getCssClass());
        }

        List<Privilege> children = privilege.getChildren();
        if (null != children && !children.isEmpty()) {
            List<Map<String, Object>> childrenList = new ArrayList<>();

            filterMenuPrivileges(includePrivilegeIds, ignoreInclude, children, childrenList);

            if (!childrenList.isEmpty()) {
                menuMap.put("children", childrenList);
            }
        }

        String firstUrl = privilege.getFirstUrl();
        if (StringUtils.isNotBlank(firstUrl)) {
            menuMap.put("url", firstUrl);
        }

        return menuMap;
    }

    private static void filterMenuPrivileges(Set<String> includePrivilegeIds, boolean ignoreInclude, List<Privilege> children, List<Map<String, Object>> childrenList) {
        for (Privilege subPrivilege : children) {

            if (ignoreInclude || isIncludePrivilegeId(includePrivilegeIds, subPrivilege.getPrivilegeId())) {

                Map<String, Object> subMenuMap = convertToMenu(subPrivilege, includePrivilegeIds, ignoreInclude);

                if (null != subMenuMap) {
                    childrenList.add(subMenuMap);
                }
            }
        }
    }

    /**
     * 将权限显示成 层级关系 + 权限标识 + 权限ID
     *
     * @param privilegeListRefParent 权限列表, 给定的需要子权限能 通过 parent 访问到父级权限
     * @param itemResultList         用来接收结果
     * @return 返回权限导航Code
     */
    public static List<String> toAuthKeyNavString(List<Privilege> privilegeListRefParent, List<PrivilegeItem> itemResultList) {
        AssertUtil.assertNotEmpty(privilegeListRefParent, "权限列表为空");

        // 所有权限，使用深度遍历
        List<Privilege> allPrivileges = new ArrayList<>();

        for (Privilege privilege : privilegeListRefParent) {
            deepIterAndAppendToAll(allPrivileges, privilege);
        }

        List<String> nameStringList = new ArrayList<>();
        List<String> authKeyStringList = new ArrayList<>();
        List<String> idStringList = new ArrayList<>();

        for (Privilege privilege : allPrivileges) {
            toAuthKeyNavString(privilege, nameStringList, authKeyStringList, idStringList);
        }

        authKeyStringList = appendBlankToPretty(authKeyStringList, 4);
        idStringList = appendBlankToPretty(idStringList, 4);

        List<String> resultList = new ArrayList<>();

        for (int i = 0; i < nameStringList.size(); ++i) {
            resultList.add(authKeyStringList.get(i) + idStringList.get(i) + nameStringList.get(i));

            if (null != itemResultList) {
                itemResultList.add(new PrivilegeItem(nameStringList.get(i), idStringList.get(i), authKeyStringList.get(i)));
            }

        }

        return resultList;
    }

    /**
     * 对齐字符串，找出最长的，然后把其他补齐相同的长度
     *
     * @param stringList       字符串列表
     * @param appendBlankCount 要补齐的数量
     * @return 返回对齐后的字符串列表
     */
    private static List<String> appendBlankToPretty(List<String> stringList, int appendBlankCount) {
        int maxLen = getStringMaxLen(stringList);

        List<String> resultList = new ArrayList<>();

        int realMaxLen = maxLen + (appendBlankCount > -1 ? appendBlankCount : 0);

        for (String string : stringList) {
            resultList.add(appendBlankByMaxLen(string, realMaxLen));
        }

        return resultList;
    }

    private static String appendBlankByMaxLen(String string, int maxLen) {
        int len = string.length();
        int appendCount = maxLen - len;
        return string + getBlank(appendCount);
    }

    private static String getBlank(int count) {
        int realCount = count < 0 ? 0 : count;
        StringBuilder builder = new StringBuilder("");
        while (realCount > 0) {
            builder.append(" ");
            realCount--;
        }
        return builder.toString();
    }

    /**
     * 获取字符串列表最长的长度
     *
     * @param stringList 字符串列表
     * @return 返回最大长度
     */
    private static int getStringMaxLen(List<String> stringList) {

        if (null == stringList || stringList.isEmpty()) {
            return 0;
        }

        int maxLen = 0;
        for (String string : stringList) {
            if (string.length() > maxLen) {
                maxLen = string.length();
            }
        }

        return maxLen;
    }

    /**
     * 将权限显示成 层级关系 + 权限标识 + 权限ID
     *
     * @param privilege 权限
     */
    private static void toAuthKeyNavString(Privilege privilege, List<String> nameStringList, List<String> authKeyStringList, List<String> idStringList) {

        if (null == privilege) {
            return;
        }

        List<Privilege> privilegeChainList = new ArrayList<>();
        privilegeChainList.add(privilege);

        Privilege parent = privilege.getParent();

        while (null != parent) {
            privilegeChainList.add(parent);
            parent = parent.getParent();
        }

        StringBuilder authKeyBuilder = new StringBuilder();
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder idBuilder = new StringBuilder();
        int len = privilegeChainList.size();

        for (int i = len - 1; i >= 0; --i) {
            Privilege priv = privilegeChainList.get(i);
            authKeyBuilder.append(priv.getPrivilegeId()).append(".");
            nameBuilder.append(priv.getName()).append("/");
            idBuilder.append(priv.getPrivilegeId()).append(".");
        }
        authKeyBuilder.setLength(authKeyBuilder.length() - 1);
        nameBuilder.setLength(nameBuilder.length() - 1);
        idBuilder.setLength(idBuilder.length() - 1);

        nameStringList.add(nameBuilder.toString());
        authKeyStringList.add(authKeyBuilder.toString());
        idStringList.add(idBuilder.toString());
    }

    /**
     * 深度遍历
     *
     * @param allPrivileges 所有权限
     * @param privilege     要遍历的权限
     */
    private static void deepIterAndAppendToAll(List<Privilege> allPrivileges, Privilege privilege) {
        allPrivileges.add(privilege);
        List<Privilege> children = privilege.getChildren();
        if (null != children && !children.isEmpty()) {
            for (Privilege childPrivilege : children) {
                deepIterAndAppendToAll(allPrivileges, childPrivilege);
            }
        }
    }

    /**
     * 转换成 javascript识别的权限
     *
     * @param privilege 权限对象
     * @param resultMap 结果MAP
     * @return 返回map
     */
    private static Map<String, Object> convertToJavaScriptReadablePrivileges(Privilege privilege, Map<String, Object> resultMap) {

        resultMap.put("id", privilege.getPrivilegeId());

        List<Privilege> children = privilege.getChildren();
        if (null != children && !children.isEmpty()) {
            for (Privilege subPrivilege : children) {

                if (resultMap.containsKey(subPrivilege.getPrivilegeId())) {
                    throw new CodeException(500, "权限[" + privilege.getPrivilegeId() + "]下面的权限标识ID[" + subPrivilege.getPrivilegeId() + "]已存在，privilegeId=" + subPrivilege.getPrivilegeId());
                }

                Map<String, Object> subMap = new HashMap<>();

                convertToJavaScriptReadablePrivileges(subPrivilege, subMap);

                resultMap.put(subPrivilege.getPrivilegeId(), subMap);
            }
        }

        return resultMap;
    }

    /**
     * 解析权限
     *
     * @param rootElement           权限项
     * @param parent                父级权限
     * @param topLevelPrivilegeList 顶级权限列表
     * @param refParent             是否指向父级
     * @return 返回权限对象
     */
    private static Privilege parsePrivilegeFromPrivilegeXml(Element rootElement, Privilege parent, List<Privilege> topLevelPrivilegeList, boolean refParent) {

        if (null == rootElement) {
            return null;
        }
        // 解析当前元素
        Privilege rootPrivilege = null;

        String privilegeId = rootElement.attributeValue("privilegeId");
        String name = rootElement.attributeValue("privilegeName");
        String showMenuString = rootElement.attributeValue("showMenu");
        String cssClass = rootElement.attributeValue("cssClass");
        boolean rootShowMenu = null != showMenuString && "true".equals(showMenuString);

        if (StringUtils.isNotBlank(privilegeId) && StringUtils.isNotBlank(name)) {
            if (StringUtils.isBlank(privilegeId)) {
                throw new CodeException(500, "权限[" + privilegeId + "] 没有设置 ID属性");
            }
            rootPrivilege = new Privilege(privilegeId, name, rootShowMenu, cssClass, null);
        }

        if (null != rootPrivilege) {
            if (null == parent) {
                topLevelPrivilegeList.add(rootPrivilege);
            } else {
                List<Privilege> children = parent.getChildren();
                if (null == children) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                children.add(rootPrivilege);

                if (refParent) {
                    rootPrivilege.setParent(parent);
                }
            }

            // 解析URL
            parsePrivilegeUrls(rootElement, rootPrivilege);

            // 解析 Ext
            parsePrivilegeExtInfo(rootElement, rootPrivilege);
        }

        List childrenPrivilegeElements = rootElement.selectNodes("privilege");

        if (childrenPrivilegeElements != null && !childrenPrivilegeElements.isEmpty()) {
            for (Object objElt : childrenPrivilegeElements) {
                Element subElement = (Element) objElt;
                parsePrivilegeFromPrivilegeXml(subElement, rootPrivilege, topLevelPrivilegeList, refParent);
            }
        }

        return rootPrivilege;

    }

    private static void parsePrivilegeExtInfo(Element rootElement, Privilege rootPrivilege) {
        List extElements = rootElement.selectNodes("ext");
        Map<String, String> extMap = new HashMap<>();
        if (extElements != null && !extElements.isEmpty()) {
            for (Object extElt : extElements) {
                Element extElement = (Element) extElt;
                String key = extElement.attributeValue("key");
                String value = extElement.attributeValue("value");
                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                    extMap.put(key, value);
                }
            }
        }
        rootPrivilege.setExt(extMap);
    }

    private static void parsePrivilegeUrls(Element rootElement, Privilege rootPrivilege) {
        List urlElements = rootElement.selectNodes("url");
        if (urlElements != null && !urlElements.isEmpty()) {
            List<String> urlList = new ArrayList<>();
            for (Object urlElt : urlElements) {
                Element urlElement = (Element) urlElt;
                String url = urlElement.getStringValue();
                if (StringUtils.isNotBlank(url)) {
                    urlList.add(url);
                }
            }
            rootPrivilege.setUrlList(urlList);
        }
    }

    private static String removeXmlBOM(String info) throws PrivilegeParseException {
        if (info == null) {
            throw new PrivilegeParseException("string could not be null");
        } else if (!info.contains("<?")) {
            throw new PrivilegeParseException("string is not in xml format");
        } else {
            return info.substring(info.indexOf("<?"));
        }
    }

    public static Set<String> getAllPrivilegeIds(List<Privilege> topPrivilegeListRefParent) {
        Set<String> privilegeIds = new HashSet<>();

        if (null != topPrivilegeListRefParent && !topPrivilegeListRefParent.isEmpty()) {
            for (Privilege privilege : topPrivilegeListRefParent) {
                addPrivilegeIdToSet(privilegeIds, privilege);
            }
        }

        return privilegeIds;
    }

    private static void addPrivilegeIdToSet(Set<String> privilegeIds, Privilege privilege) {
        if (null != privilege) {
            privilegeIds.add(String.valueOf(privilege.getPrivilegeId()));

            List<Privilege> children = privilege.getChildren();
            if (null != children && !children.isEmpty()) {
                for (Privilege child : children) {
                    addPrivilegeIdToSet(privilegeIds, child);
                }
            }
        }
    }

    /**
     * 变成一个List，使用广度遍历
     *
     * @param topPrivileges 顶级权限列表
     * @return 返回权限列表
     */
    public static List<Privilege> extractToList(List<Privilege> topPrivileges) {
        List<Privilege> allPrivileges = new ArrayList<>();

        LinkedList<Privilege> queue = new LinkedList<>(topPrivileges);
        while (!queue.isEmpty()) {
            List<Privilege> tempList = new ArrayList<>(queue);
            allPrivileges.addAll(tempList);
            queue.clear();
            for (Privilege privilege : tempList) {
                List<Privilege> children = privilege.getChildren();
                if (null != children && !children.isEmpty()) {
                    queue.addAll(children);
                }
            }
        }

        return allPrivileges;
    }

    public static Map<String, Privilege> extractToUrlPrivilegeMap(List<Privilege> allPrivileges) {
        Map<String, Privilege> urlPrivilegeMap = new HashMap<>();

        for (Privilege privilege : allPrivileges) {
            List<String> urlList = privilege.getUrlList();

            if (urlList == null || urlList.isEmpty()) {
                continue;
            }

            for (String url : urlList) {
                urlPrivilegeMap.put(url, privilege);
            }

        }

        return urlPrivilegeMap;
    }

    public static Map<String, Privilege> extractToIdPrivilegeMap(List<Privilege> allPrivileges) {
        Map<String, Privilege> idPrivilegeMap = new HashMap<>();

        for (Privilege privilege : allPrivileges) {
            idPrivilegeMap.put(privilege.getPrivilegeId(), privilege);
        }

        return idPrivilegeMap;
    }
}
