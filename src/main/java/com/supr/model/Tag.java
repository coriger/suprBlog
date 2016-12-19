package com.supr.model;

/**
 * @desc	标签
 * @author	ljt
 * @time	2015-8-28 下午4:26:21
 */
public class Tag {
	
	private Integer id;
	
	private String tagName;

    private int sum;

    public Tag() {
    }

    public Tag(Integer id) {
        this.id = id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
}
