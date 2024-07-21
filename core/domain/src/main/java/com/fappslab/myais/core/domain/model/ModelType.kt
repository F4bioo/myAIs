package com.fappslab.myais.core.domain.model

// https://ai.google.dev/gemini-api/docs/models/gemini?hl=pt-br#model-variations
enum class ModelType(val model: String) {
    GEMINI_1_5_PRO(model = "gemini-1.5-pro"),
    GEMINI_1_5_FLASH(model = "gemini-1.5-flash"),

    @Deprecated(
        message = "Gemini 1.0 Pro Vision has been deprecated on July 12, 2024. Consider switching to different model, for example gemini-1.5-flash.",
        replaceWith = ReplaceWith(expression = "GeminiType.GEMINI_1_5_FLASH")
    )
    GEMINI_PRO_VISION(model = "gemini-pro-vision")
}
