# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key         => '_epgp_session',
  :secret      => '97d9c868811e4d789bf921cfc64ba7180e2cf1240bed79ab8ece5c2ce78887803eb33f566448a55991d3dc7d56ee93adc3aeb03248fa26cce680fc66ce86a368'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store
