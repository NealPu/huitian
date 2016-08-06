package com.momathink.sys.dict.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.momathink.sys.dict.model.Dict;


/** 字典
 */
public class DictTag extends TagSupport {

	private static final long serialVersionUID = -3647113162613485011L;


	/**
     * Dict中的key值
     */
    private String numbers;
    
    public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	@Override
    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().write(Dict.dao.queryValGetDictname(numbers));
        } catch (IOException e) {
            return Tag.SKIP_BODY;
        }
        return Tag.EVAL_BODY_INCLUDE;
    }
 
}

