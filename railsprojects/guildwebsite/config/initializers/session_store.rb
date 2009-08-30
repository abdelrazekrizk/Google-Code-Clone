# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key         => '_guildwebsite_session',
  :secret      => 'c77c046ebd81689e3ce7079edbcbf23979ed71943a088bd74890d3cd466f04942811dee22903226eb3fbe2e972d20042504889379436969e45dcb2382c3b1b48'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store
