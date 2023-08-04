package framework.model.util.printGen;

import java.awt.Font;
import java.io.Serializable;

public class PrintPosDetailBean implements Serializable {
	private String type;
	private Object data;
	private int x;
	private int y;
	private int height;
	private int width;
	private  Font font;
	private String align; 
	private boolean isBackground;
	
	public PrintPosDetailBean(Object data, int x, int y, Font font){ 
		this.type = "T";
		this.data = data;
		this.x = x;
		this.y = y;
		this.font = font;
	}
	
	public PrintPosDetailBean(Object data, int x, int y, Font font, String align){
		this.type = "T";
		this.data = data;
		this.x = x;
		this.y = y;
		this.font = font;
		this.align = align;
	}
	public PrintPosDetailBean(Object data, int x, int y, int width, int height, String align){
		this.type = "I";
		this.data = data;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.align = align;
	}
	public PrintPosDetailBean(int x, int y, int a, int b){
		this.type = "S";
		this.x = x;
		this.y = y;
		this.width = a;
		this.height = b;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}

	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isBackground() {
		return isBackground;
	}

	public void setIsBackground(boolean isBackground) {
		this.isBackground = isBackground;
	}
}
