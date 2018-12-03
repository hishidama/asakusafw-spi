AsakusaFW Hishidama SPI
=======================
`Asakusa Framework <http://www.ne.jp/asahi/hishidama/home/tech/asakusafw/index.html>`_ のひしだま拡張SPIです。

DMDLからテンプレートを使ったJavaソースの生成が出来ます。


インストール方法
----------------
Asakusaプロジェクトのbuild.gradleに以下のようなリポジトリーおよび依存関係を追加して、Eclipseプロジェクト情報を再作成して下さい。

.. sourcecode:: gradle

 repositories {
     maven { url 'http://hishidama.github.io/mvnrepository' }
 }
 
 dependencies {
 ～
     compile(group: 'jp.hishidama.asakusafw', name: 'asakusafw-spi', version: '0.+') {
         exclude group: 'com.asakusafw'
         exclude group: 'org.apache.hadoop'
     }
 ～
 }


使用例
------
DMDLに ``@template`` という属性でテンプレートファイルを指定することで、テンプレートファイルを使ってJavaソースファイルが生成できます。

DMDLファイル（dmdl）
~~~~~~~~~~~~~~~~~~~~
.. sourcecode:: dmdl

   "サンプル"
   @directio.csv
   @template(
     id = "fromCsv",
     template = "src/main/template/FromCsv.ftl",
     category = "csv",
     type_name_pattern = "{0}FromCsv",
     args = { "dataSize=LARGE" }
   )
   example1 = {
   
       foo : TEXT;
   
       bar : INT;
   };

@template属性には以下のような値を指定します。

* id: 1つのデータモデルに複数の ``@template`` が指定できるので、その区別をする為のID。
* template: テンプレートファイルの場所。プロジェクトディレクトリーからの相対パス。
* category: 生成されるJavaソースのパッケージ名の一部。
* type_name_pattern: 生成されるJavaのクラス名。
* args: 自由な引数。省略可。

テンプレートファイル（ftl）
~~~~~~~~~~~~~~~~~~~~~~~~~~~
.. sourcecode:: ftl

   package ${packageName};
   
   /**
    * ${model.description!""}
    */
   public class ${className} extends Abstract${model.camelName}CsvInputDescription {
   
       @Override
       public String getBasePath() {
           return "master/${model.name}";
       }
   
       @Override
       public String getResourcePattern() {
           return "${model.name}.csv";
       }
   
       @Override
       public DataSize getDataSize() {
           return DataSize.${arg("dataSize")!"UNKOWN"};
       }
   }

テンプレートには `FreeMarker <http://www.ne.jp/asahi/hishidama/home/tech/java/freemarker/index.html>`_ を使います。

生成されたJavaソースファイル
~~~~~~~~~~~~~~~~~~~~~~~~~~~~
.. sourcecode:: java

   package com.example.modelgen.dmdl.csv;
   
   /**
    * サンプル
    */
   public class Example1FromCsv extends AbstractExample1CsvInputDescription {
   
       @Override
       public String getBasePath() {
           return "master/example1";
       }
   
       @Override
       public String getResourcePattern() {
           return "example1.csv";
       }
   
       @Override
       public DataSize getDataSize() {
           return DataSize.LARGE;
       }
   }

DMDLのコンパイルを行うと、（他のJavaソースの生成と同様に）テンプレートを使ったJavaソースも生成されます。

