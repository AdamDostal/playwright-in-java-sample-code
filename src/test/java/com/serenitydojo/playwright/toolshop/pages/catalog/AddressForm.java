package com.serenitydojo.playwright.toolshop.pages.catalog;

import com.microsoft.playwright.Page;

public class AddressForm {
    private final Page page;

    public AddressForm(Page page) {
        this.page = page;
    }

    public void confirmAddress() {
        page.getByTestId("proceed-3").click();
    }
}