/**
 * SessionStorage 对象提供了一种简单的会话存储解决方案，
 * 允许我们在一个浏览器会话期间存储键值对数据。
 */
SessionStorage = {
    /**
     * 从会话存储中获取指定键的值。
     * @param {string} key - 需要获取值的键名。
     * @returns 返回解码后的 JSON 对象，如果不存在则返回 undefined。
     */
    get: function (key) {
        var v = sessionStorage.getItem(key);
        // 检查值是否存在且不是 "undefined" 字符串
        if (v && typeof(v) !== "undefined" && v !== "undefined") {
            return JSON.parse(v); // 解析存储的字符串为 JSON 对象
        }
    },
    /**
     * 将数据存储到会话存储中指定的键。
     * @param {string} key - 用于存储数据的键名。
     * @param {Object} data - 需要存储的数据，将被转换为 JSON 字符串。
     */
    set: function (key, data) {
        // 将 JavaScript 对象或值转换为 JSON 字符串后存储
        sessionStorage.setItem(key, JSON.stringify(data));
    },
    /**
     * 从会话存储中移除指定键的值。
     * @param {string} key - 需要移除的键名。
     */
    remove: function (key) {
        sessionStorage.removeItem(key);
    },
    /**
     * 清除会话存储中的所有数据。
     */
    clearAll: function () {
        sessionStorage.clear();
    }
};
