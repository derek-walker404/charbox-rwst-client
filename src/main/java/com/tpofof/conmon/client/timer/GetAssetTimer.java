package com.tpofof.conmon.client.timer;

import org.apache.commons.httpclient.methods.GetMethod;

public class GetAssetTimer extends AssetTimer<GetMethod> {

	@Override
	protected GetMethod getHttpMethod(String assetLocation) {
		return new GetMethod(assetLocation);
	}

}
