package domain;

import java.util.HashMap;

/**
 *
 * @author Frederik
 */
public class ExcelData {

    private HashMap<Integer, String> data = new HashMap<>();

    public ExcelData() {
    }

    public void setData(int columnIndex, String data) {
	this.data.put(columnIndex, data);
    }

    public String getData(int columnIndex) {
	return this.data.get(columnIndex);
    }

    public int getSize() {
	return this.data.size();
    }
}
