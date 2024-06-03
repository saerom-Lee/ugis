/**
 * 데이터 저장 네임스페이스
 */
CMSC003.Storage = (() => {
  let storage_ = {};
  return {
    set(key, value) {
      storage_[key] = value;
    },

    get(key) {
      return storage_[key];
    },

    reset() {
      storage_ = {};
    }
  }
})();