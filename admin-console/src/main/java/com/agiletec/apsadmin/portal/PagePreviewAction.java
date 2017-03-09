package com.agiletec.apsadmin.portal;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.page.IPageTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;

public class PagePreviewAction extends AbstractPortalAction {

	private static final Logger _logger = LoggerFactory.getLogger(PagePreviewAction.class);

	private String pageCode;
	private String lang;
	private String size;
	private String token;

	private IPageTokenManager pageTokenMager; 
	private IUserManager userManager;

	public String getPageCode() {
		return pageCode;
	}
	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	protected IPageTokenManager getPageTokenMager() {
		return pageTokenMager;
	}
	public void setPageTokenMager(IPageTokenManager pageTokenMager) {
		this.pageTokenMager = pageTokenMager;
	}

	protected IUserManager getUserManager() {
		return userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}

	public String getPreviewToken(String pageCode) {
		if (StringUtils.isNotBlank(pageCode)) {
			return this.pageTokenMager.encrypt(this.getPageCode());
		}
		return null;
	}

	public String preview() {
		try {
			String ERR_RESULT = "apslogin";
			if (null == this.getCurrentUser()) {
				UserDetails guest = this.getUserManager().getGuestUser();
				this.getRequest().getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, guest);
			}
			if (StringUtils.isBlank(this.getPageCode())) {
				_logger.info("Null page code");
				this.addActionError(this.getText("error.page.invalidPageCode"));
				return ERR_RESULT;
			}
			if (!isCurrentUserAdmin()) {
				boolean isValidToken = this.checkToken(this.getToken(), this.getPageCode());
				if (!isValidToken) {
					_logger.info("Invalid token");
					this.addActionError(this.getText("error.page.invalidPreviewToken"));
					return ERR_RESULT;
				}
			}
			IPage page = this.getPage(this.getPageCode());
			if (!this.getAuthorizationManager().isAuth(this.getCurrentUser(), page)) {
				_logger.info("Curent user not allowed");
				this.addActionError(this.getText("error.page.userNotAllowed"));
				return ERR_RESULT;
			}
			if (null == page) {
				_logger.info("Null page code");
				this.addActionError(this.getText("error.page.invalidPageCode"));
				return ERR_RESULT;
			}

			if (null == this.getLangManager().getLang(this.getLang())) {
				String defaultLangCode = this.getLangManager().getDefaultLang().getCode();
				_logger.warn("Invalid lang '{}' detected. Defaulting to '{}'", this.getLang(), defaultLangCode);
				this.setLang(defaultLangCode);
			}
		} catch (Exception e) {
			_logger.error("error in preview", e);
			return FAILURE;
		}
		return SUCCESS;
	}

	public IPage getPage(String pageCode) {
		Page page = null;
		IPage currentPage = this.getPageManager().getDraftPage(pageCode);
		if (null != currentPage) {
			page = new Page();
			page.setCode(currentPage.getCode());
			page.setParent(currentPage.getParent());
			page.setParentCode(currentPage.getParentCode());
			page.setGroup(currentPage.getGroup());
//			page.setPosition(currentPage.getPosition());
			PageMetadata metadata = currentPage.getDraftMetadata();
			page.setDraftMetadata(metadata);
			page.setOnlineMetadata(metadata);
			IPage[] children = currentPage.getAllChildren();
			page.setChildren(children);
			page.setAllChildren(children);
			Widget[] widgets = currentPage.getDraftWidgets();
			page.setDraftWidgets(widgets);
			page.setOnlineWidgets(widgets);
		}
		return page;
	}

	public ScreenSize getScreenSize() {
		ScreenSize screenSize = new ScreenSize(this.getSize());
		return screenSize;
	}

	protected boolean isCurrentUserAdmin() {
		return this.getAuthorizationManager().isAuthOnPermission(this.getCurrentUser(), Permission.SUPERUSER);
	}

	protected boolean checkToken(String token, String expected) {
		if (StringUtils.isNotBlank(this.getToken())) {
			String page = this.getPageTokenMager().decrypt(token);
			return null != page && page.trim().equalsIgnoreCase(expected);
		}
		return false;
	}

}
