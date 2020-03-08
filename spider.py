import requests
import re

#爬取《圣墟》小说所有章节

url='http://www.xbiquge.la/13/13959/' #小说目录网页网址 

#requests模块帮助我们模拟浏览器去访问网页并获取服务器响应结果，不用关心底层实现
response=requests.get(url)
response.encoding='utf-8' #注意网页编码问题

catalogHtml=response.text #获取目录网页的html源代码

#获取小说名字
title=re.findall(r'<meta property="og:title" content="(.*?)"/>',catalogHtml)[0]

#新建一个txt文件保存小说内容
fb=open('%s.txt' %title,'w',encoding='utf-8')

#用正则表达式在html源代码中匹配每一章的url和章名，需要会看html源代码
chapterDiv=re.findall(r'<div id="list">.*?</div>',catalogHtml,re.S)[0]
#形成章节列表，注意单引号里的单引号需要转义
chapter_list=re.findall(r'href=\'(.*?)\' >(.*?)<',chapterDiv)
#循环遍历列表，爬取每章章节内容
for chapter in chapter_list:
    chapter_url='http://www.xbiquge.la'+chapter[0] #注意补全章节完整域名
    chapter_title=chapter[1]
    fb.write(chapter_title+'\n')
    print(chapter_url)
fb.close()
