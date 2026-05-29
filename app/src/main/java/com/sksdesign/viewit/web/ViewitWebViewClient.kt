package com.sksdesign.viewit.web

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.sksdesign.viewit.adblock.AdblockEngine
import com.sksdesign.viewit.adblock.RequestClassifier
import java.io.ByteArrayInputStream

class ViewitWebViewClient(
    private val adblockEngine: AdblockEngine,
    private val adblockEnabledForApp: Boolean,
    private val preferMobileLayout: Boolean,
    private val forceNoHorizontalOverflow: Boolean = false,
    private val viewportFixMode: String = "generic",
    private val onLoadingChanged: (Boolean) -> Unit,
    private val onError: (String) -> Unit,
    private val onUrlChanged: (String) -> Unit
) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean = false

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        onLoadingChanged(true)
        url?.let(onUrlChanged)
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        onLoadingChanged(false)
        url?.let(onUrlChanged)
        val webView = view
        if (forceNoHorizontalOverflow && webView != null) {
            val script = scriptForViewportFix(viewportFixMode)
            webView.evaluateJavascript(script, null)
            webView.postDelayed({ webView.evaluateJavascript(script, null) }, 600)
            webView.postDelayed({ webView.evaluateJavascript(script, null) }, 1600)
            webView.postDelayed({ webView.evaluateJavascript(script, null) }, 3200)
        }
        super.onPageFinished(view, url)
    }

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        val requestUrl = request?.url?.toString().orEmpty()
        val type = RequestClassifier.classify(requestUrl)
        return if (adblockEnabledForApp && adblockEngine.isEnabled() && adblockEngine.shouldBlock(requestUrl, type)) {
            WebResourceResponse("text/plain", "utf-8", ByteArrayInputStream(ByteArray(0)))
        } else {
            super.shouldInterceptRequest(view, request)
        }
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        if (request?.isForMainFrame == true) onError(error?.description?.toString() ?: "The page could not be loaded.")
        super.onReceivedError(view, request, error)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        handler?.cancel()
        onError("The page reported an SSL error. VIEWit did not ignore this error.")
    }

    companion object {
        private fun scriptForViewportFix(mode: String): String {
            return if (mode == "tv_service_fit") TV_SERVICE_FIT_SCRIPT else NO_HORIZONTAL_OVERFLOW_SCRIPT
        }

        private const val NO_HORIZONTAL_OVERFLOW_SCRIPT = """
            (function(){
                var style=document.getElementById('viewit-no-horizontal-overflow');
                if(!style){
                    style=document.createElement('style');
                    style.id='viewit-no-horizontal-overflow';
                    style.textContent='html,body{max-width:100vw!important;overflow-x:hidden!important;} body{position:relative!important;}';
                    document.documentElement.appendChild(style);
                }
                document.documentElement.style.overflowX='hidden';
                if(document.body){document.body.style.overflowX='hidden';}
                window.scrollTo(0, window.scrollY);
            })();
        """

        private const val TV_SERVICE_FIT_SCRIPT = """
            (function(){
                var style=document.getElementById('viewit-tv-service-fit');
                if(!style){
                    style=document.createElement('style');
                    style.id='viewit-tv-service-fit';
                    style.textContent='html,body{margin:0!important;padding:0!important;background:#000!important;overflow-x:hidden!important;max-width:100vw!important;} *,*:before,*:after{box-sizing:border-box!important;} #root,#app,main,[role="main"],[data-testid="root"],.Root,.Root__main-view,.Root__top-container{max-width:100vw!important;overflow-x:hidden!important;}';
                    document.documentElement.appendChild(style);
                }
                var meta=document.querySelector('meta[name="viewport"]');
                if(!meta){meta=document.createElement('meta');meta.name='viewport';document.head.appendChild(meta);}
                meta.setAttribute('content','width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no');
                function fitViewitTvService(){
                    var doc=document.documentElement;
                    var body=document.body;
                    if(!body){return;}
                    var viewportWidth=Math.max(doc.clientWidth || 0, window.innerWidth || 0);
                    var scrollWidth=Math.max(doc.scrollWidth || 0, body.scrollWidth || 0, viewportWidth);
                    var ratio=1;
                    if(scrollWidth > viewportWidth + 2){
                        ratio=Math.max(0.82, Math.min(1, (viewportWidth - 2) / scrollWidth));
                    }
                    doc.style.width='100vw';
                    doc.style.maxWidth='100vw';
                    doc.style.overflowX='hidden';
                    doc.style.background='#000';
                    body.style.margin='0';
                    body.style.padding='0';
                    body.style.maxWidth='none';
                    body.style.overflowX='hidden';
                    body.style.background='#000';
                    if(ratio < 0.999){
                        body.style.transformOrigin='top left';
                        body.style.transform='scale(' + ratio + ')';
                        body.style.width=(100 / ratio) + 'vw';
                        body.style.minWidth=(100 / ratio) + 'vw';
                        body.style.minHeight=(100 / ratio) + 'vh';
                    } else {
                        body.style.transform='none';
                        body.style.width='100vw';
                        body.style.minWidth='100vw';
                        body.style.minHeight='100vh';
                    }
                    var nodes=document.querySelectorAll('#root,#app,main,[role="main"],[data-testid="root"],.Root,.Root__main-view,.Root__top-container');
                    for(var i=0;i<nodes.length;i++){
                        nodes[i].style.maxWidth='100vw';
                        nodes[i].style.overflowX='hidden';
                    }
                    window.scrollTo(0, window.scrollY);
                }
                fitViewitTvService();
                setTimeout(fitViewitTvService,300);
                setTimeout(fitViewitTvService,900);
                setTimeout(fitViewitTvService,1800);
            })();
        """
    }
}
