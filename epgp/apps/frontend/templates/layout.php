<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
    <?php include_http_metas() ?>
    <?php include_metas() ?>
    <?php include_title() ?>
    <link rel="shortcut icon" href="/favicon.ico" />
  </head>
  <body>
    <div align="center">
      <table width="800" height="307" border="0">
        <tr>
          <td height="19" colspan="2">&nbsp;</td>
        </tr>
        <tr>
          <td width="265" height="112" rowspan="2">
            <div>
              <a href="<?php echo url_for('@homepage'); ?>">
                <img src="/images/epgplogo.png" width="267" height="112" />
              </a>
            </div>
          </td>
          <td width="442" height="112">&nbsp;</td>
        </tr>
        <tr>
          <td height="19">
            <div id="navmenu">
              <ul>
                <li><?php echo link_to('Standings', 'roster/index'); ?></li>
                <li><?php echo link_to('Raids', 'raid/index'); ?></li>
                <li><?php echo link_to('Bosses', 'boss/index'); ?></li>
                <li><?php echo link_to('Zones', 'zone/index'); ?></li>
                <li><?php echo link_to('Items', 'items/index'); ?></li>
                <li><?php echo link_to('Decay', 'decays/index'); ?></li>
                <li><?php echo link_to('Statistics', 'stats/index'); ?></li>
<?php if ($sf_user->isAuthenticated()): ?>
                <li><?php echo link_to('Logout', '@sf_guard_signout'); ?></li>
<?php else: ?>
                <li><?php echo link_to('Login', '@sf_guard_signin'); ?></li>
<?php endif; ?>
              </ul>
            </div>
          </td>
        </tr>
<?php if($sf_user->isAuthenticated() && $sf_user->hasCredential('admin')): ?>
        <tr class="adminbar">
          <td height="40" colspan="2">
            <div id="adminbarlist">
              <ul>
                <li><b>&lt;&lt;Admin&gt;&gt;</b></li>
                <li><?php echo link_to('Home', 'admin/home'); ?></li>
                <li><?php echo link_to('Import', 'admin/import'); ?></li>
                <li><?php echo link_to('Roster', 'admin/roster'); ?></li>
                <li><?php echo link_to('Raids', 'admin/raids'); ?></li>
                <li><?php echo link_to('Attendees', 'admin/attendees')?></li>
                <li><?php echo link_to('Decays', 'admin/decays'); ?></li>
                <li><?php echo link_to('Items', 'admin/items'); ?></li>
                <li><?php echo link_to('Bosses', 'admin/bosses'); ?></li>
                <li><?php echo link_to('Zones', 'admin/zones'); ?></li>
                <li><?php echo link_to('Users', '@sf_guard_user'); ?></li>
              </ul>
            </div>
          </td>
        </tr>
<?php endif; ?>
        <tr class="content">
          <td colspan="2">
<?php echo $sf_content ?>
          </td>
        </tr>
      </table>
    </div>
  </body>
</html>
