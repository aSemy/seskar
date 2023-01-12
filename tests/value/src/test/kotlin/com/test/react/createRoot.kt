package com.test.react

import react.VFC
import react.create
import react.dom.client.Root
import react.dom.client.createRoot
import react.dom.test.act
import web.html.HTMLElement

suspend fun createRoot(
    container: HTMLElement,
    component: VFC,
): Root =
    act {
        val root = createRoot(container)
        root.render(component.create())
        root
    }
