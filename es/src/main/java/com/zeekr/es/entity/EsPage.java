package com.zeekr.es.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author 59899
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class EsPage<T> {
    /**
     * 当前页
     */
    private long currentPage;
    /**
     * 每页显示多少条
     */
    private long pageSize;
    /**
     * 总记录数
     */
    private long recordCount;
    /**
     * 本页的数据列表
     */
    private List<T> recordList;
    /**
     * 总页数
     */
    private long pageCount;
    /**
     * 页码列表的开始索引（包含）
     */
    private long beginPageIndex;
    /**
     * 页码列表的结束索引（包含）
     */
    private long endPageIndex;

    /**
     * 只接受前4个必要的属性，会自动的计算出其他3个属性的值
     * @param currentPage 当前页
     * @param pageSize 每页显示多少条
     * @param recordCount 总记录数
     * @param recordList 本页的数据列表
     */
    public EsPage(long currentPage, long pageSize, long recordCount, List<T> recordList) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.recordCount = recordCount;
        this.recordList = recordList;
        // 计算总页码
        pageCount = (recordCount + pageSize - 1) / pageSize;
        // 计算 beginPageIndex 和 endPageIndex
        // 总页数不多于10页，则全部显示
        if (pageCount <= 10) {
            beginPageIndex = 1;
            endPageIndex = pageCount;
        }
        // 总页数多于10页，则显示当前页附近的共10个页码
        else {
            // 当前页附近的共10个页码（前4个 + 当前页 + 后5个）
            beginPageIndex = currentPage - 4;
            endPageIndex = currentPage + 5;
            // 当前面的页码不足4个时，则显示前10个页码
            if (beginPageIndex < 1) {
                beginPageIndex = 1;
                endPageIndex = 10;
            }
            // 当后面的页码不足5个时，则显示后10个页码
            if (endPageIndex > pageCount) {
                endPageIndex = pageCount;
                beginPageIndex = pageCount - 10 + 1;
            }
        }
    }


}
