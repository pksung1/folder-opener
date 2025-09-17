  Pod::Spec.new do |s|
    s.name = 'Pksung1FolderOpener'
    s.version = '1.0.0'
    s.summary = 'Capacitor plugin for opening folders containing a specified file'
    s.license = 'MIT'
    s.homepage = 'https://github.com/pksung1/folder-opener'
    s.author = 'Seon Park'
    s.source = { :path => '.' }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target = '14.0'
    s.dependency 'Capacitor'
  end
