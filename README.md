# **Web Crawler Implementation**
  The project presented is an implementation in order to better understand the functiona Web Crawler must have. A Web Crawler is a bot useful for web indexing. It crawls one page at a time from a starting URL address, until all pages that derive from the URL address have been indexed or a certain depth for crawling was reached. 
  These resources can be stored locally (our instance) or in the Cloud, and the user will be notified when the Crawler has finished crawling and indexing the sites specified, afterwards being fitting for data analyzing methods:
* data filtering by typeor size;
* creating a sitemap;
* searching using key-words.

# **User Manual**
  The application uses a combination of arguments depending on the desired result, the application has a predefined configuration but it can be overwritten with a custom configuration file, respecting the file format from the Software Requirements Specification:
  
  Crawl the sites from an input file:
```
crawl -s sites.txt [-conf config.txt]
```

  Create the sitemaps for the stored sites and outputting them to a file:
```
sitemap [-conf config.txt]
```

  Filter the contents of the stored resources by type, and deleting the files that do not meet the criteria:
```
filter_type [-conf config.txt]
```

  Filter the contents of the stored resources by size, and deleting the files that do not meet the criteria:
```
filter_size [-conf config.txt]
```

  Search the contents of the stored resources by a series of key-words and outputting them to a file:
```
search [-conf config.txt] <key_words>
```

# **Documentation**
  The documentation provided is as follows:
The *Software Requirements Specification*: https://github.com/alexalbu98/Crawler/blob/master/SRS.docx
The *Software Design Description*: https://github.com/alexalbu98/Crawler/blob/master/SDD.docx
The *Testing Documentation*: **to be uploaded**
