/**    
 * @{#} BindView.java Create on 2014年9月30日 上午9:33:52    
 *    
 * @author <a href="mailto:evan0502@qq.com">Evan</a>   
 * @version 1.0    
 * @description
 *	
 */    
package com.e.common.utility.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindView {

	public int id();

    public boolean click() default false;
}
