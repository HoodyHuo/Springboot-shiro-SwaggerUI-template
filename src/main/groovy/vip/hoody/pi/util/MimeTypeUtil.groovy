package vip.hoody.pi.util

class MimeTypeUtil {

    final static Map MIME_TYPE = [
            "image/bmp"        : ".bmp",
            "image/jpeg"       : ".jpg",
            "image/pict"       : ".pic",
            "image/png"        : ".png", //Defined in [RFC-2045], [RFC-2048]
            "image/x-png"      : ".png", //See https://www.w3.org/TR/PNG/#A-Media-type :"It is recommended that implementations also recognize the media type "image/x-png"."
            "image/tiff"       : ".tiff",
            "image/x-macpaint" : ".mac",
            "image/x-quicktime": ".qti"
    ]

    static String getSuffixByMimeType(String mimeType) {
        return MIME_TYPE.get(mimeType)
    }

}
