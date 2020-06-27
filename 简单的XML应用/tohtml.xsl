<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl" xmlns:s="https://zunhuier.club">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>图书信息管理系统</title>
            </head>
            <body>
                <table border="1" style="margin:0 auto" >
                    <tr bgcolor="pink">
                        <td>书名</td><td>作者</td><td>分类</td><td>出版时间</td><td>价格</td>
                    </tr>
                    <xsl:for-each select="s:library/s:book">
                        <tr>
                            <td><xsl:value-of select="name"/></td>
                            <td><xsl:value-of select="author"/></td>
                            <td><xsl:value-of select="class"/></td>
                            <td><xsl:value-of select="publicTime"/></td>
                            <td><xsl:value-of select="price"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>