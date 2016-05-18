/**    
 * @{#} SectionItem.java Create on 2014-6-9 下午7:29:32    
 *          
 * @author <a href="mailto:evan0502@qq.com">Evan</a>   
 * @version 1.0    
 */
package com.e.common.widget.sectionlist;

public class SectionRow {

	public static final int ITEM = 0;
	public static final int SECTION = 1;

	public int type = ITEM;
	public Object obj;
    public Object obj1;
	public Object obj2;

	public int sectionPosition;
	public int listPosition;

	// 标志当前记录是否为分类下的第一条
	public boolean isFirst;

	public SectionRow(int type, Object obj) {
		this.type = type;
		this.obj = obj;
	}

	public SectionRow(int type,Object obj,Object obj1){
		this.type = type;
		this.obj = obj;
		this.obj1 = obj1;
	}

	public SectionRow(){

	}

	public SectionRow(int type) {
		this.type = type;
	}

	public SectionRow(int type,Object obj,Object obj1,Object obj2){
		this.type = type;
		this.obj = obj;
		this.obj1 = obj1;
		this.obj2 = obj2;
	}

	@Override
	public String toString() {
		return obj.toString();
	}
}
