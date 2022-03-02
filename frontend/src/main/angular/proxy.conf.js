module.exports = [
  {
    context: ['/api/**'],
    target: 'http://localhost:8080',
    secure: false,
    logLevel: 'debug'
    //  https://angular.io/guide/build#proxying-to-a-backend-server
  }
]
// ng serve --proxy-config proxy.conf.js
