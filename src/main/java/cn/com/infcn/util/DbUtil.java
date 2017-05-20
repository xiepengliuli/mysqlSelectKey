package cn.com.infcn.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DbUtil {

	private static ComboPooledDataSource ILASDataSource = new ComboPooledDataSource();// 使用默认的配置

	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = ILASDataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void colse(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Object> getData(String key) {
		Connection connection = DbUtil.getConnection();
		List<String> tablesList = null;
		try {
			tablesList = gettableNames(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<String> sqlList = new ArrayList<String>();
		for (String tableName : tablesList) {
			List<String> colums = null;
			try {
				colums = getColumsByTname(connection, tableName);
				String sql = "";
				for (String string : colums) {
					if (sql.length() <= 0) {
						sql += " select * from " + tableName + " where " + string + " like '%" + key + "%' ";
						// sql += " select * from " + tableName + " where " +
						// string + " like '" + key + "' ";
						// sql += " select * from " + tableName + " where " +
						// string + "='" + key + "' ";
					} else {
						sql += " or " + string + " like '%" + key + "%' ";
						// sql += " or " + string + " like '" + key + "' ";
						// sql += " or " + string + "='" + key + "' ";
					}
				}
				System.out.println(sql + ";");
				sqlList.add(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		Map<String, Object> mapList = getMapList(connection, sqlList);

		DbUtil.colse(connection);

		return mapList;
	}

	private static Map<String, Object> getMapList(Connection connection, List<String> sqlList) {
		QueryRunner queryRunner = new QueryRunner();
		Map<String, Object> tempMap = new HashMap<String, Object>();
		for (String sql : sqlList) {
			try {

				List<Map<String, Object>> queryResult = queryRunner.query(connection, sql, new MapListHandler());
				if (queryResult != null && queryResult.size() > 0) {
					tempMap.put(sql, queryResult);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return tempMap;
	}

	private static List<String> getColumsByTname(Connection connection, String tableName) throws Exception {
		ResultSet resultSet = connection.getMetaData().getColumns(null, null, tableName, "%");
		List<String> columns = new ArrayList<String>();
		while (resultSet.next()) {
			String columnName = resultSet.getString("COLUMN_NAME");
			String DATA_TYPE = resultSet.getString("DATA_TYPE");
			if (DATA_TYPE.equals("1111") || DATA_TYPE.equals("93") || DATA_TYPE.equals("91")) {
				continue;
			}

			columns.add(columnName);
		}
		return columns;
	}

	private static List<String> gettableNames(Connection connection) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet tables = metaData.getTables(null, "ILAS", "%", new String[] { "TABLE" });
		List<String> tablesList = new ArrayList<String>();
		while (tables.next()) {
			tablesList.add(tables.getString("TABLE_NAME"));
		}
		return tablesList;
	}

}
