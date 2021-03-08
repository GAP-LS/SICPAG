<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="text-center">
	<c:if test="${pages > 1}">
		<ul class="pagination">
			<c:set var="paginationFirst"
				value="./${template}?page=1${empty busca ? '' : '&busca='.concat(busca)}" />
			<c:set var="paginationPrevious"
				value="./${template}?page=${page - 1}${empty busca ? '' : '&busca='.concat(busca)}" />
			<c:set var="paginationNext"
				value="./${template}?page=${page + 1}${empty busca ? '' : '&busca='.concat(busca)}" />
			<c:set var="paginationLast"
				value="./${template}?page=${pages}${empty busca ? '' : '&busca='.concat(busca)}" />
			<li ${page == 1 ? 'class="disabled"' : ''}><a class="aglyph"
				href="${page == 1 ? '#' : paginationFirst}" data-toggle="tooltip"
				data-placement="top" title="Primeiro"> <span
					class="glyphicon glyphicon-fast-backward" aria-hidden="true"></span>
			</a></li>
			<li ${page == 1 ? 'class="disabled"' : ''}><a class="aglyph"
				href="${page == 1 ? '#' : paginationPrevious}" data-toggle="tooltip"
				data-placement="top" title="Anterior"> <span
					class="glyphicon glyphicon-step-backward" aria-hidden="true"></span>
			</a></li>
			<c:forEach
				begin="${page - paginationBefore < 1 ? 1 : page - paginationBefore}"
				end="${page + paginationAfter > pages ? pages : page + paginationAfter}"
				var="i">
				<c:set var="paginationLink" value="./${template}?page=${i}" />
				<li ${page == i ? 'class="active"' : ''}><a
					href="${page == i ? '#' : paginationLink}${empty busca ? '' : '&busca='.concat(busca)}">${i}</a></li>
			</c:forEach>
			<li ${page == pages ? 'class="disabled"' : ''}><a class="aglyph"
				href="${page == pages ? '#' : paginationNext}" data-toggle="tooltip"
				data-placement="top" title="Próximo"> <span
					class="glyphicon glyphicon-step-forward" aria-hidden="true"></span>
			</a></li>
			<li ${page == pages ? 'class="disabled"' : ''}><a class="aglyph"
				href="${page == pages ? '#' : paginationLast}" data-toggle="tooltip"
				data-placement="top" title="Último"> <span
					class="glyphicon glyphicon-fast-forward" aria-hidden="true"></span>
			</a></li>
		</ul>
	</c:if>
</div>