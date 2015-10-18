/**
 * Copyright 2011-2015 Asakusa Framework Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.jobflow.port;

import java.util.Arrays;
import java.util.List;

import com.asakusafw.runtime.directio.DataFormat;
import com.asakusafw.vocabulary.directio.DirectFileOutputDescription;
import com.example.jobflow.format.CategorySummaryExcelFormat;
import com.example.modelgen.dmdl.model.CategorySummary;

/**
 * カテゴリ別集計結果をDirect I/Oで出力する。 
 * 出力ファイルは {@code result/category} 上の {@code result.xls}。
 */
public class CategorySummaryToExcel extends DirectFileOutputDescription {
	@Override
	public Class<?> getModelType() {
		return CategorySummary.class;
	}

	@Override
	public Class<? extends DataFormat<?>> getFormat() {
		// Excel用のDataFormat
		return CategorySummaryExcelFormat.class;
	}

	@Override
	public String getBasePath() {
		return "result/category";
	}

	@Override
	public String getResourcePattern() {
		return "result.xls";
	}

	@Override
	public List<String> getOrder() {
		return Arrays.asList("-selling_price_total");
	}
}
