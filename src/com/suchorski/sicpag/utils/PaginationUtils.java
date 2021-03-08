package com.suchorski.sicpag.utils;

import javax.servlet.http.HttpServletRequest;

public class PaginationUtils {
	
	private static final int MAX_ROWS_PER_PAGE = 50;
	private static final int MAX_NUMBER_PAGINATION = 11;
	
	public static PaginationInfoUtil paginate(HttpServletRequest request) {
		int page = 1, maxRowsPerPage = MAX_ROWS_PER_PAGE, offset = 0, maxNumberPagination = MAX_NUMBER_PAGINATION;
		try {
			page = Integer.parseInt(request.getParameter("page"));
			offset = (page - 1) * maxRowsPerPage;
		} catch (NumberFormatException e) {
			page = 1;
		} finally {
			int before = (int) Math.ceil(maxNumberPagination / 2);
			int after = maxNumberPagination - before;
			request.setAttribute("paginationBefore", before);
			request.setAttribute("paginationAfter", after);
		}
		return new PaginationInfoUtil(page, maxRowsPerPage, offset);
	}
	
	public static class PaginationInfoUtil {
		
		public int page;
		public int maxRowsPerPage;
		public int offset;
		
		public PaginationInfoUtil(int page, int maxRowsPerPage, int offset) {
			this.page = page;
			this.maxRowsPerPage = maxRowsPerPage;
			this.offset = offset;
		}
		
		public int numPages(int size) {
			return (int) Math.ceil(1.0 * size / maxRowsPerPage);
		}
		
	}

}
