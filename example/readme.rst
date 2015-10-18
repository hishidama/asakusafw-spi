AsakusaFW Hishidama SPI サンプル
================================
`Asakusa Framework <http://www.ne.jp/asahi/hishidama/home/tech/asakusafw/index.html>`_ の
`ひしだま拡張SPI <https://github.com/hishidama/asakusafw-spi>`_ のサンプルです。

.. sourcecode:: gradle

 repositories {
     maven { url 'http://hishidama.github.io/mvnrepository' }
 }
 
 dependencies {
 ～
     compile group: 'jp.hishidama.asakusafw', name: 'asakusafw-spi-runtime', version: '0.+'
 ～
 }


Direct I/O Excelファイル入出力の例
----------------------------------
Direct I/OでExcelファイルを読み書きするImporter/ExporterおよびDataFormatの例です。
（使用ライブラリー： `asakusafw-spi-runtime` ）

* store_info
  * src/main/java/ `com.example.jobflow.port.StoreInfoFromExcel`
  * src/main/java/ `com.example.jobflow.format.StoreInfoExcelFormat`
* category_summary
  * src/main/java/ `com.example.jobflow.port.CategorySummaryToExcel`
  * src/main/java/ `com.example.jobflow.format.CategorySummaryExcelFormat` …出力する際に、テンプレートとなるExcelファイルを使用する
  * src/main/resources/com/example/jobflow/format/ `category_summary.xls`

※この方法でExcelファイルの入出力が出来ますが、基本的には出力に使うことを想定しています。
（Excelファイルを読み込みたい場合は、 `Embulk <http://www.embulk.org/plugins/#file-parser>`_ 等でExcelファイルをcsvファイル等に変換してAsakusaFWで読み込む方が素直だと思います。
AsakusaFWはデータモデルの形式に合うデータの方が読みやすい（分散もしやすい）ので、AsakusaFWへ渡す前の処理として、AsakusaFWが読める形式に変換するのが良いと思います）


