/**
 * @author qiweiwei
 *
 */
package com.sh3h.localprovider;

/**
 * MeterReadingDataProviderManager
 */
public class DBManager {

	private static IDataProvider _dataProvider = null;

	/**
	 * 获取IGreenDaoMeterReadingDataProvider实例
	 *
	 * @return
	 */
	public static IDataProvider getInstance() {
		if (_dataProvider == null) {
			synchronized (IDataProvider.class){
				if(_dataProvider == null){
					_dataProvider = new DataProviderImpl();
				}
			}
		}
		return _dataProvider;
	}
}
