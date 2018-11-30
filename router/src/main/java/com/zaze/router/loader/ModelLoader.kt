package com.zaze.router.loader

import com.zaze.router.anno.Model
import java.util.*

/**
 * Description : 加载所有Router
 *
 * @author : ZAZE
 * @version : 2018-11-27 - 10:02
 */
class ModelLoader(val rootPackageName: String) {

    fun loadInto(cls: Class<*>, models: HashMap<Class<*>, String>): Boolean {
        cls.getAnnotation(Model::class.java)?.let {
            val modelPath = it.path.replace("/", ".")
            if (modelPath.startsWith(rootPackageName)) {
                models[cls] = modelPath
            } else {
                models[cls] = rootPackageName + modelPath
            }
            return true
        }
        return false
    }
}
