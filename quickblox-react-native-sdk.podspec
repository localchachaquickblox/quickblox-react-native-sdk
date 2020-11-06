require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name						= package['name']
  s.version					= package['version']
  s.summary					= "RNQbReactnative"
  s.homepage				= package['homepage']
  s.license					= package['license']
  s.author					= { "Injoit LTD" => "sales@quickblox.com" }
  s.platform				= :ios, '10.0'
  s.source					= { :git => "https://github.com/QuickBlox/quickblox-react-native-sdk-internal.git", :tag => "without-wml" }
  s.preserve_paths	= 'README.md', 'package.json', 'index.js'
  s.source_files        = 'ios/*/**/*.{h,m}'
  s.requires_arc		= true

  s.dependency 'QuickBlox', '~> 2.17.4'
  s.dependency 'Quickblox-WebRTC', '~> 2.7.5'
  s.dependency 'React', '>= 0.60.0'
end

  
