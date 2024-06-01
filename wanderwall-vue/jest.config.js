module.exports = {
  testEnvironment: 'jsdom',
  moduleFileExtensions: ['js', 'jsx', 'json', 'vue'],
  transform: {
    '^.+\\.(vue)$': '<rootDir>/node_modules/@vue/vue3-jest',
    '^.+\\.(js|jsx|mjs|cjs|ts|tsx)$': '<rootDir>/node_modules/babel-jest',
    '\\.(jpg|jpeg|png|gif|svg)$': 'jest-transform-stub',
  },
  transformIgnorePatterns: ['/node_modules/'],
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1',
    '\\.(css|less|sass|scss)$': 'identity-obj-proxy',
    '\\.(jpg|jpeg|png|gif|svg)$': 'jest-transform-stub',
    "\\.(mp4)$": "<rootDir>/tests/mockVideo.js",
  },
  testMatch: [
    '**/tests/unit/*.spec.js',
  ],
  'testURL': 'http://localhost',
  reporters: [
    'default',
    ['jest-junit', {
      outputDirectory: './junit',
      outputName: 'junit.xml',
    }],
  ],
};