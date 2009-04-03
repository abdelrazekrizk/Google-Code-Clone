<?php

require_once(sfConfig::get('sf_lib_dir').'/filter/base/BaseFormFilterPropel.class.php');

/**
 * Roster filter form base class.
 *
 * @package    epgp
 * @subpackage filter
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormFilterGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseRosterFormFilter extends BaseFormFilterPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'name'      => new sfWidgetFormFilterInput(),
      'charclass' => new sfWidgetFormPropelChoice(array('model' => 'Classes', 'add_empty' => true)),
      'ep'        => new sfWidgetFormFilterInput(),
      'gp'        => new sfWidgetFormFilterInput(),
      'priority'  => new sfWidgetFormFilterInput(),
      'joined_on' => new sfWidgetFormFilterDate(array('from_date' => new sfWidgetFormDate(), 'to_date' => new sfWidgetFormDate(), 'with_empty' => false)),
      'is_active' => new sfWidgetFormChoice(array('choices' => array('' => 'yes or no', 1 => 'yes', 0 => 'no'))),
    ));

    $this->setValidators(array(
      'name'      => new sfValidatorPass(array('required' => false)),
      'charclass' => new sfValidatorPropelChoice(array('required' => false, 'model' => 'Classes', 'column' => 'id')),
      'ep'        => new sfValidatorSchemaFilter('text', new sfValidatorNumber(array('required' => false))),
      'gp'        => new sfValidatorSchemaFilter('text', new sfValidatorNumber(array('required' => false))),
      'priority'  => new sfValidatorSchemaFilter('text', new sfValidatorNumber(array('required' => false))),
      'joined_on' => new sfValidatorDateRange(array('required' => false, 'from_date' => new sfValidatorDate(array('required' => false)), 'to_date' => new sfValidatorDate(array('required' => false)))),
      'is_active' => new sfValidatorChoice(array('required' => false, 'choices' => array('', 1, 0))),
    ));

    $this->widgetSchema->setNameFormat('roster_filters[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'Roster';
  }

  public function getFields()
  {
    return array(
      'id'        => 'Number',
      'name'      => 'Text',
      'charclass' => 'ForeignKey',
      'ep'        => 'Number',
      'gp'        => 'Number',
      'priority'  => 'Number',
      'joined_on' => 'Date',
      'is_active' => 'Boolean',
    );
  }
}
