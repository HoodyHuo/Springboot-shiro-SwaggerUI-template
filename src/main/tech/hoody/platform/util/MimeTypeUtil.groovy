package tech.hoody.platform.util

class MimeTypeUtil {

    final static Map<String, String> MIME_TYPE = [
            "image/bmp"        : ".bmp",
            "image/jpeg"       : ".jpg",
            "image/pict"       : ".pic",
            "image/png"        : ".png", //Defined in [RFC-2045], [RFC-2048]
            "image/x-png"      : ".png", //See https://www.w3.org/TR/PNG/#A-Media-type :"It is recommended that implementations also recognize the media type "image/x-png"."
            "image/tiff"       : ".tiff",
            "image/x-macpaint" : ".mac",
            "image/x-quicktime": ".qti"
    ]
    static Map Suffix = null

    static String getSuffixByMimeType(String mimeType) {
        return MIME_TYPE.get(mimeType)
    }

    static String getMimeTypeBySuffix(String suffix) {
        if (Suffix == null) {
            produce()
        }
        return Suffix.get(suffix)
    }

    private static produce() {
        MIME_TYPE.keySet().collect { String key ->
            Suffix.put(MIME_TYPE.get(key), key)
        }
    }

}
