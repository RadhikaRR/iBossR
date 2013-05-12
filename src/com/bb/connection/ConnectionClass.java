package com.bb.connection;

import java.io.IOException;

import net.rim.device.api.system.CoverageInfo;

import com.bb.customClass.LogEventClass;

public class ConnectionClass {

	public static ConnectionClass INSTANCE = new ConnectionClass();

	public ConnectionClass() {
		// TODO Auto-generated constructor stub
	}

	public boolean checkNetworkConnection() {
		if ((CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS))
				|| (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B))
				|| (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_DIRECT)) != false) {
			return true;
		}
		return false;
	}

	public String connectTCP(String url) throws IOException {
		LogEventClass.logAlwaysEvent("TCP is selected and control is inside TCP connection");
		TransportDetective detective = new TransportDetective();

		int availableTransportCoverage = detective.getAvailableTransportCoverage();

		if (availableTransportCoverage == 0) {
			throw new IOException("No network coverage available");
		}

		URLFactory urlFactory = new URLFactory(url);

		if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_WIFI)) {
			url = urlFactory.getHttpTcpWiFiUrl();
			LogEventClass.logAlwaysEvent("TRANSPORT_TCP_WIFI is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_CELLULAR)) {
			url = url + ";deviceside=true";
			LogEventClass.logAlwaysEvent("TRANSPORT_TCP_CELLULAR is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.DEFAULT_TCP_CELLULAR)) {
			url = urlFactory.getHttpDefaultTcpCellularUrl(detective.getDefaultTcpCellularServiceRecord());
			LogEventClass.logAlwaysEvent("DEFAULT_TCP_CELLULAR is available with url is " + url);
		} else {
			//throw new IOException("No Supported Network coverage detected");
		}
		return url;
	}

	public String connectBIS(String url) throws IOException {
		LogEventClass.logAlwaysEvent("BIS is selected and control is inside BIS connection");
		TransportDetective detective = new TransportDetective();

		int availableTransportCoverage = detective.getAvailableTransportCoverage();

		if (availableTransportCoverage == 0) {
			throw new IOException("No network coverage available");
		}

		URLFactory urlFactory = new URLFactory(url);

		if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_TCP_WIFI)) {
			url = urlFactory.getHttpTcpWiFiUrl();
			LogEventClass.logAlwaysEvent("TRANSPORT_TCP_WIFI is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_BIS_B)) {
			url = urlFactory.getHttpBisUrl();
			LogEventClass.logAlwaysEvent("TRANSPORT_BIS_B is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.DEFAULT_TCP_CELLULAR)) {
			url = urlFactory.getHttpDefaultTcpCellularUrl(detective.getDefaultTcpCellularServiceRecord());
			LogEventClass.logAlwaysEvent("DEFAULT_TCP_CELLULAR is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_MDS)) {
			url = urlFactory.getHttpMdsUrl(false);
			LogEventClass.logAlwaysEvent("TRANSPORT_MDS is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_WAP2)) {
			url = urlFactory.getHttpWap2Url(detective.getWap2ServiceRecord());
			LogEventClass.logAlwaysEvent("TRANSPORT_WAP2 is available with url is " + url);
		} else {
			throw new IOException("No Supported Network coverage detected");
		}
		return url;
	}

	public String connectOther(String url) throws IOException {
		LogEventClass.logAlwaysEvent("Other selected and control is inside connectOther connection");
		TransportDetective detective = new TransportDetective();

		int availableTransportCoverage = detective.getAvailableTransportCoverage();

		if (availableTransportCoverage == 0) {
			throw new IOException("No Transport coverage detected");
		}

		URLFactory urlFactory = new URLFactory(url);

		if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_MDS)) {
			url = urlFactory.getHttpMdsUrl(false);
			LogEventClass.logAlwaysEvent("TRANSPORT_MDS is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.TRANSPORT_WAP2)) {
			url = urlFactory.getHttpWap2Url(detective.getWap2ServiceRecord());
			LogEventClass.logAlwaysEvent("TRANSPORT_WAP2 is available with url is " + url);
		} else if (detective.isCoverageAvailable(TransportDetective.DEFAULT_TCP_CELLULAR)) {
			url = urlFactory.getHttpDefaultTcpCellularUrl(detective.getDefaultTcpCellularServiceRecord());
			LogEventClass.logAlwaysEvent("DEFAULT_TCP_CELLULAR is available with url is " + url);
		} else {
			throw new IOException("No Supported Transport coverage detected");
		}
		return url;
	}
}
