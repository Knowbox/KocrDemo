# KOcrDemo

Knowbox Ocr Demo

## 基本使用

- 在 App Moudle 的 `build.gradle` 文件中引入 `tesseract` 的 Android 移植版本.

	```
	dependencies {
	    compile 'com.rmtheis:tess-two:8.0.0'
	}
	```
	
	
- 下载相应语言的[训练数据集](https://github.com/tesseract-ocr/tessdata), 将其存放在项目的 `assets` 目录下

- 初始化 `TessBaseAPI` 

	```
	 TessBaseAPI tessBaseAPI = new TessBaseAPI();
	 /**
      * mDataPath:本地存储训练数据集
      * lang: 要解析的语言
      */
     boolean suc = tessBaseAPI.init(mDataPath, lang);
	```

- 解析图片中的文本

	```
	if (suc) {
        tessBaseAPI.setImage(mBitmap);
        content = tessBaseAPI.getUTF8Text();
        tessBaseAPI.end();
    } 
	```

## 高级用法

- 训练新语言 见[文档](./训练新语言.md)
	
## 更多文档

- 训练数据集地址: [tesseract-ocr/tessdata](https://github.com/tesseract-ocr/tessdata)
- tesseract 地址: [tesseract-ocr/tesseract](https://github.com/tesseract-ocr/tesseract)
- Android 平台移植版本地址: [rmtheis/tess-two](https://github.com/rmtheis/tess-two)