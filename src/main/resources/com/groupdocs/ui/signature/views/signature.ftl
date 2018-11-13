<#-- @ftlvariable name="" type="Signature" -->
<!DOCTYPE html>
<html>
    <head>
        <title>Signature for Java Dropwizard</title>
        <link rel="stylesheet" href="/assets/common/css/all.min.css">
        <link rel="stylesheet" href="/assets/common/css/v4-shims.min.css">
        <link type="text/css" rel="stylesheet" href="/assets/common/css/swiper.min.css">
        <link type="text/css" rel="stylesheet" href="/assets/common/css/jquery-ui.min.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/common/css/circle-progress.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/viewer/css/viewer.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/viewer/css/viewer.mobile.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/viewer/css/viewer-dark.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/signature/css/bcPaint.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/signature/css/bcPaint.mobile.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/signature/css/signature.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/signature/css/signature.mobile.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/signature/css/stampGenerator.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/signature/css/opticalCodeGenerator.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/signature/css/textGenerator.css"/>
        <link type="text/css" rel="stylesheet" href="/assets/signature/css/bcPicker.css"/>
        <script type="text/javascript" src="/assets/common/js/jquery.min.js"></script>
        <script type="text/javascript" src="/assets/common/js/swiper.min.js"></script>
        <script type="text/javascript" src="/assets/common/js/jquery-ui.min.js"></script>
        <script type="text/javascript" src="/assets/viewer/js/viewer.js"></script>
        <script type="text/javascript" src="/assets/signature/js/signature.js"></script>
        <script type="text/javascript" src="/assets/signature/js/rotatable.js"></script>
        <script type="text/javascript" src="/assets/signature/js/bcPaint.js"></script>
        <script type="text/javascript" src="/assets/signature/js/bcPicker.js"></script>
        <script type="text/javascript" src="/assets/signature/js/stampGenerator.js"></script>
        <script type="text/javascript" src="/assets/signature/js/opticalCodeGenerator.js"></script>
        <script type="text/javascript" src="/assets/signature/js/textGenerator.js"></script>
        <script type="text/javascript" src="/assets/common/js/jquery.ui.touch-punch.min.js"></script>
    </head>
    <body>
        <div id="element"></div>
        <script type="text/javascript">
            $('#element').viewer({
                applicationPath: 'http://${globalConfiguration.server.hostAddress}:${globalConfiguration.server.httpPort?c}/signature',
                defaultDocument: '${globalConfiguration.signature.defaultDocument}',
                htmlMode: false,
                preloadPageCount: ${globalConfiguration.signature.preloadPageCount?c},
                zoom : false,
                pageSelector: ${globalConfiguration.common.pageSelector?c},
                search: false,
                thumbnails: false,
                rotate: false,
                download: ${globalConfiguration.common.download?c},
                upload: ${globalConfiguration.common.upload?c},
                print: ${globalConfiguration.common.print?c},
                browse: ${globalConfiguration.common.browse?c},
                rewrite: ${globalConfiguration.common.rewrite?c}
            });
            $('#element').signature({
                textSignature: ${globalConfiguration.signature.textSignature?c},
                imageSignature:  ${globalConfiguration.signature.imageSignature?c},
                digitalSignature:  ${globalConfiguration.signature.digitalSignature?c},
                qrCodeSignature:  ${globalConfiguration.signature.qrCodeSignature?c},
                barCodeSignature:  ${globalConfiguration.signature.barCodeSignature?c},
                stampSignature:  ${globalConfiguration.signature.stampSignature?c},
                downloadOriginal:  ${globalConfiguration.signature.downloadOriginal?c},
                downloadSigned:  ${globalConfiguration.signature.downloadSigned?c}
            });
        </script>
    </body>
</html>