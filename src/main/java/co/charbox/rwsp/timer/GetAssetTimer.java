package co.charbox.rwsp.timer;

import org.apache.commons.httpclient.methods.GetMethod;

public class GetAssetTimer extends AssetTimer<GetMethod> {

	@Override
	protected GetMethod getHttpMethod(String assetLocation) {
		return new GetMethod(assetLocation);
	}

}
