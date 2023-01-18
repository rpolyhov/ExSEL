// @ts-check

/** @type {import('@playwright/test').PlaywrightTestConfig} */
const config = {
  use: {
    headless: false,
    viewport: { width: 1920, height: 1080 },
    ignoreHTTPSErrors: true,
    video: {
          mode: 'on',
          size: { width: 1280, height: 720 }
        },
    screenshot: 'on',
  },
};
module.exports = config;
